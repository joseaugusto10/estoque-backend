package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.exceptions.EstoqueInvalidoException;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.repositories.MovimentoEstoqueRepository;
import br.com.nexdom.estoque_backend.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimentoEstoqueServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentoEstoqueRepository movimentoEstoqueRepository;

    @InjectMocks
    private MovimentoEstoqueService movimentoEstoqueService;

    @Test
    void registrarEntrada() {

        // aqui deve somar estoque, salvar produto e registrar movimento com dataMovimento preenchida
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Mouse");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("50.00"));
        produto.setEstoque(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenReturn(produto);
        when(movimentoEstoqueRepository.save(any(MovimentoEstoque.class))).thenAnswer(inv -> inv.getArgument(0));

        MovimentoEstoque mov = movimentoEstoqueService.registrarMovimento(
                1L, TipoMovimentacao.ENTRADA, 5, null, null
        );

        assertNotNull(mov);
        assertEquals(TipoMovimentacao.ENTRADA, mov.getTipoMovimentacao());
        assertEquals(5, mov.getQtdMovimentada());
        assertNull(mov.getValorVenda());
        assertNull(mov.getDataVenda());
        assertNotNull(mov.getDataMovimento());
        assertEquals(15, produto.getEstoque());

        verify(produtoRepository).save(produto);
        verify(movimentoEstoqueRepository).save(any(MovimentoEstoque.class));
    }

    @Test
    void registrarSaida() {

        //  aqui deve diminuir o estoque, salvar produto e registrar movimento com valorVenda, dataVenda e dataMovimento
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Teclado");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("30.00"));
        produto.setEstoque(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenReturn(produto);
        when(movimentoEstoqueRepository.save(any(MovimentoEstoque.class))).thenAnswer(inv -> inv.getArgument(0));

        BigDecimal valorVenda = new BigDecimal("100.00");
        LocalDateTime dataVenda = LocalDateTime.of(2026, 2, 22, 10, 0);

        MovimentoEstoque mov = movimentoEstoqueService.registrarMovimento(
                1L, TipoMovimentacao.SAIDA, 4, valorVenda, dataVenda
        );

        assertNotNull(mov);
        assertEquals(TipoMovimentacao.SAIDA, mov.getTipoMovimentacao());
        assertEquals(4, mov.getQtdMovimentada());
        assertEquals(valorVenda, mov.getValorVenda());
        assertEquals(dataVenda, mov.getDataVenda());
        assertNotNull(mov.getDataMovimento());
        assertEquals(6, produto.getEstoque());

        verify(produtoRepository).save(produto);
        verify(movimentoEstoqueRepository).save(any(MovimentoEstoque.class));
    }

    @Test
    void registrarSaidaSemSaldo() {

        // aqui deve lançar exceção e não salvar nada quando não houver saldo suficiente
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Monitor");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("500.00"));
        produto.setEstoque(3);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        EstoqueInvalidoException ex = assertThrows(
                EstoqueInvalidoException.class,
                () -> movimentoEstoqueService.registrarMovimento(
                        1L, TipoMovimentacao.SAIDA, 10, new BigDecimal("900.00"), LocalDateTime.now()
                )
        );

        assertEquals("Saldo insuficiente. Estoque atual: 3, Saida solicitada: 10", ex.getMessage());

        verify(produtoRepository, never()).save(any());
        verify(movimentoEstoqueRepository, never()).save(any());
    }

    @Test
    void registrarProdutoInexistente() {

        // aqui deve lançar exceção quando produto não existir
        when(produtoRepository.findById(80L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> movimentoEstoqueService.registrarMovimento(
                        80L, TipoMovimentacao.ENTRADA, 1, null, null
                )
        );

        assertTrue(ex.getMessage().contains("Produto não encontrado"));

        verify(produtoRepository, never()).save(any());
        verify(movimentoEstoqueRepository, never()).save(any());
    }

    @Test
    void registrarSaidaSemValorVenda() {

        // aqui deve lançar exceção quando saída não informar valorVenda
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Cadeira");
        produto.setTipoProduto(TipoProduto.MOVEL);
        produto.setValorNoFornecedor(new BigDecimal("80.00"));
        produto.setEstoque(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        EstoqueInvalidoException ex = assertThrows(
                EstoqueInvalidoException.class,
                () -> movimentoEstoqueService.registrarMovimento(
                        1L, TipoMovimentacao.SAIDA, 1, null, LocalDateTime.now()
                )
        );

        assertEquals("Valor de venda é obrigatório para saída.", ex.getMessage());

        verify(produtoRepository, never()).save(any());
        verify(movimentoEstoqueRepository, never()).save(any());
    }

}