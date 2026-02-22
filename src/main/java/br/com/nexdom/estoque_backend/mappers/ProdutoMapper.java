package br.com.nexdom.estoque_backend.mappers;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoCreateRequest;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoResponse;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoUpdateRequest;

public final class ProdutoMapper {

    private ProdutoMapper() {}

    public static Produto toEntity(ProdutoCreateRequest produtoCreateRequest) {
        Produto produto = new Produto();
        produto.setDescricao(produtoCreateRequest.getDescricao());
        produto.setTipoProduto(produtoCreateRequest.getTipoProduto());
        produto.setValorNoFornecedor(produtoCreateRequest.getValorNoFornecedor());
        produto.setEstoque(produtoCreateRequest.getEstoque());
        return produto;
    }

    public static Produto toEntity(ProdutoUpdateRequest produtoUpdateRequest) {
        Produto produto = new Produto();
        produto.setDescricao(produtoUpdateRequest.getDescricao());
        produto.setTipoProduto(produtoUpdateRequest.getTipoProduto());
        produto.setValorNoFornecedor(produtoUpdateRequest.getValorNoFornecedor());
        produto.setEstoque(produtoUpdateRequest.getEstoque());
        return produto;
    }

    public static ProdutoResponse toResponse(Produto produto) {
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setCodigo(produto.getCodigo());
        produtoResponse.setDescricao(produto.getDescricao());
        produtoResponse.setTipoProduto(produto.getTipoProduto());
        produtoResponse.setValorNoFornecedor(produto.getValorNoFornecedor());
        produtoResponse.setEstoque(produto.getEstoque());
        return produtoResponse;
    }
}
