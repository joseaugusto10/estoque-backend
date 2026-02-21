package br.com.nexdom.estoque_backend.repositories;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

    @Query("""
        select coalesce(sum(m.qtdMovimentada), 0)
        from MovimentoEstoque m
        where m.produto.codigo = :produtoId
          and m.tipoMovimentacao = :tipo
    """)
    long somarQuantidadePorProdutoETipo(@Param("produtoId") Long produtoId,
                                        @Param("tipo") TipoMovimentacao tipo);

}
