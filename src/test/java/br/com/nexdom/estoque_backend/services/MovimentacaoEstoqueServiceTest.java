package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovimentacaoEstoqueServiceTest {

    @Test
    public void registrarMovimentacao(){

        int estoque = 5;
        int qtd = 10;
        TipoMovimentacao tpEntrada = TipoMovimentacao.ENTRADA;
        TipoMovimentacao tpSaida = TipoMovimentacao.SAIDA;

       MovimentacaoEstoqueService ms = new MovimentacaoEstoqueService();

       estoque = ms.registrarMovimento(estoque, qtd, tpEntrada);

        assertEquals(
            15, estoque, "Estoque atual: " + estoque
        );

    }

}
