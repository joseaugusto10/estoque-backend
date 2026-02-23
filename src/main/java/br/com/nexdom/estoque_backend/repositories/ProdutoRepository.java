package br.com.nexdom.estoque_backend.repositories;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.dtos.produto.LucroProdutoResponse;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByTipoProduto(TipoProduto tipoProduto, Pageable pageable);

    Page<Produto> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    boolean existsByDescricaoIgnoreCase(String descricao);

    @Query("""
        select new br.com.nexdom.estoque_backend.dtos.produto.ProdutoResumoResponse(
            p.codigo,
            p.descricao,
            p.tipoProduto,
            p.valorNoFornecedor,
            p.estoque,
            coalesce(sum(m.qtdMovimentada), 0)
        )
        from Produto p
        left join MovimentoEstoque m
            on m.produto.codigo = p.codigo
           and m.tipoMovimentacao = :tipoMovimentacao
        where (:tipo is null or p.tipoProduto = :tipo)
        group by p.codigo, p.descricao, p.tipoProduto, p.valorNoFornecedor, p.estoque
    """)
    Page<ProdutoResumoResponse> listarResumoPorTipo(
            @Param("tipo") TipoProduto tipo,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao,
            Pageable pageable
    );

    @Query("""
        select new br.com.nexdom.estoque_backend.dtos.produto.LucroProdutoResponse(
            p.codigo,
            p.descricao,
            p.tipoProduto,
            coalesce(sum(m.qtdMovimentada), 0),
            coalesce(sum((m.valorVenda - p.valorNoFornecedor) * m.qtdMovimentada), 0)
        )
        from Produto p
        left join MovimentoEstoque m
            on m.produto.codigo = p.codigo
           and m.tipoMovimentacao = :tipoMovimentacao
        where p.codigo = :codigoProduto
        group by p.codigo, p.descricao, p.tipoProduto
    """)
    Optional<LucroProdutoResponse> consultarLucroPorProduto(
            @Param("codigoProduto") Long codigoProduto,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao
    );

}