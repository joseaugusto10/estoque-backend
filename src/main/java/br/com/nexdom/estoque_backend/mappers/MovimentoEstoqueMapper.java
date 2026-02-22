package br.com.nexdom.estoque_backend.mappers;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.dtos.movimento.MovimentoEstoqueResponse;

public final class MovimentoEstoqueMapper {

    private MovimentoEstoqueMapper() {}

    // CreateRequest aqui não vira entity direto, porque o service precisa buscar produto e aplicar regra
    // Então esse mapper não cria entity, ele só mapeia Response.

    public static MovimentoEstoqueResponse toResponse(MovimentoEstoque movimentoEstoque) {
        MovimentoEstoqueResponse movimentoEstoqueResponse = new MovimentoEstoqueResponse();

        movimentoEstoqueResponse.setCodigoMovimentacao(movimentoEstoque.getCodigoMovimentacao());
        movimentoEstoqueResponse.setCodigoProduto(movimentoEstoque.getProduto() != null ? movimentoEstoque.getProduto().getCodigo() : null);
        movimentoEstoqueResponse.setTipoMovimentacao(movimentoEstoque.getTipoMovimentacao());
        movimentoEstoqueResponse.setQtdMovimentada(movimentoEstoque.getQtdMovimentada());
        movimentoEstoqueResponse.setValorVenda(movimentoEstoque.getValorVenda());
        movimentoEstoqueResponse.setDataVenda(movimentoEstoque.getDataVenda());
        movimentoEstoqueResponse.setDataMovimento(movimentoEstoque.getDataMovimento());

        if (movimentoEstoque.getProduto() != null) {
            movimentoEstoqueResponse.setEstoqueAtual(movimentoEstoque.getProduto().getEstoque());
        }

        return movimentoEstoqueResponse;
    }
}
