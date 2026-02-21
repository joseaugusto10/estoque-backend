package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.exceptions.EstoqueInvalidoException;

public class MovimentacaoEstoqueService {


    public int registrarMovimento(int estoqueAtual, int qtd, TipoMovimentacao tipo){
        validarParametros(estoqueAtual, qtd, tipo);
        return switch (tipo) {
            case ENTRADA -> estoqueAtual + qtd;
            case SAIDA -> {
                validarSaldoParaSaida(estoqueAtual, qtd);
                yield estoqueAtual - qtd;
            }
            default -> throw new EstoqueInvalidoException("Tipo de movimentação inválido: " + tipo);
        };
    }

    private void validarParametros(int estoqueAtual, int qtd, TipoMovimentacao tipo) {
        if (tipo == null) {
            throw new EstoqueInvalidoException("Tipo de movimentação é obrigatório.");
        }
        if (estoqueAtual < 0) {
            throw new EstoqueInvalidoException("Estoque atual não pode ser negativo: " + estoqueAtual);
        }
        if (qtd <= 0) {
            throw new EstoqueInvalidoException("Quantidade deve ser maior que zero. Informado: " + qtd);
        }
    }

    private void validarSaldoParaSaida(int estoqueAtual, int qtd) {
        if (estoqueAtual < qtd) {
            throw new EstoqueInvalidoException(
                    "Saldo insuficiente. Estoque atual: " + estoqueAtual + ", Saida solicitada: " + qtd
            );
        }
    }

}
