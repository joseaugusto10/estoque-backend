package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.exceptions.EstoqueInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovimentoEstoqueServiceTest {

    private final MovimentoEstoqueService ms = new MovimentoEstoqueService();

    @Test
    void realizarEntrada() {
        int estoque = 5;
        int qtd = 10;
        int resultado = ms.registrarMovimento(estoque, qtd, TipoMovimentacao.ENTRADA);
        assertEquals(15, resultado);
    }

    @Test
    void realizarSaida() {
        int estoque = 15;
        int qtd = 5;
        int resultado = ms.registrarMovimento(estoque, qtd, TipoMovimentacao.SAIDA);
        assertEquals(10, resultado);
    }

    @Test
    void lancarExcecaoSaidaSemSaldo() {
        int estoque = 10;
        int qtd = 30;
        EstoqueInvalidoException ex = assertThrows(
                EstoqueInvalidoException.class,
                () -> ms.registrarMovimento(estoque, 30, TipoMovimentacao.SAIDA)
        );
        assertEquals("Saldo insuficiente. Estoque atual: 10, Saida solicitada: 30", ex.getMessage());
    }

}
