package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;

public class MovimentacaoEstoqueService {


    public int registrarMovimento(int estoqueAtual, int qtd, TipoMovimentacao tipo){

        if (tipo == TipoMovimentacao.ENTRADA && qtd > 0){
            return estoqueAtual + qtd;
        }
        return 0;
    }

}
