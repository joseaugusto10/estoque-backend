package br.com.nexdom.estoque_backend.mappers;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.dtos.movimento.MovimentoEstoqueResponse;

public final class MovimentoEstoqueMapper {

    private MovimentoEstoqueMapper() {}

    // CreateRequest aqui não vira entity direto, porque o service precisa buscar produto e aplicar regra
    // Então esse mapper não cria entity, ele só mapeia Response.

    public static MovimentoEstoqueResponse toResponse(MovimentoEstoque movimentoEstoque) {
        MovimentoEstoqueResponse response = new MovimentoEstoqueResponse();

        response.setCodigoMovimentacao(movimentoEstoque.getCodigoMovimentacao());
        response.setTipoMovimentacao(movimentoEstoque.getTipoMovimentacao());
        response.setQtdMovimentada(movimentoEstoque.getQtdMovimentada());
        response.setValorVenda(movimentoEstoque.getValorVenda());
        response.setDataVenda(movimentoEstoque.getDataVenda());
        response.setDataMovimento(movimentoEstoque.getDataMovimento());

        Produto produto = movimentoEstoque.getProduto();

        if (produto != null) {
            response.setCodigoProduto(produto.getCodigo());
            response.setDescricaoProduto(produto.getDescricao());
            response.setEstoqueAtual(produto.getEstoque());
        }

        return response;
    }
}