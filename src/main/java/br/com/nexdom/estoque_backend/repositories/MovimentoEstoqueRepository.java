package br.com.nexdom.estoque_backend.repositories;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

    @EntityGraph(attributePaths = "produto")
    Page<MovimentoEstoque> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "produto")
    Optional<MovimentoEstoque> findById(Long id);

    @EntityGraph(attributePaths = "produto")
    Page<MovimentoEstoque> findByProdutoCodigo(Long codigoProduto, Pageable pageable);

    @EntityGraph(attributePaths = "produto")
    Page<MovimentoEstoque> findByTipoMovimentacao(TipoMovimentacao tipoMovimentacao, Pageable pageable);

    @EntityGraph(attributePaths = "produto")
    Page<MovimentoEstoque> findByProdutoCodigoAndTipoMovimentacao(
            Long codigoProduto,
            TipoMovimentacao tipoMovimentacao,
            Pageable pageable
    );

    boolean existsByProdutoCodigo(Long codigoProduto);

    @Modifying
    @Query("delete from MovimentoEstoque m where m.produto.codigo = :codigoProduto")
    void deleteByProdutoCodigo(Long codigoProduto);

}