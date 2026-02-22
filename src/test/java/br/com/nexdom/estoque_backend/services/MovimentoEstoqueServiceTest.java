package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.dtos.movimento.MovimentoEstoqueResponse;
import br.com.nexdom.estoque_backend.exceptions.EstoqueInvalidoException;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.mappers.MovimentoEstoqueMapper;
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
class MovimentoEstoqueServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentoEstoqueRepository movimentoEstoqueRepository;

    @InjectMocks
    private MovimentoEstoqueService movimentoEstoqueService;

    @Test
    void registrarEntrada() {
        Produto produto = produtoPadrao(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
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
        Produto produto = produtoPadrao(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
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
    void registrarSaidaComDataVendaNull() {
        Produto produto = produtoPadrao(10);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(movimentoEstoqueRepository.save(any(MovimentoEstoque.class))).thenAnswer(inv -> inv.getArgument(0));

        MovimentoEstoque mov = movimentoEstoqueService.registrarMovimento(
                1L, TipoMovimentacao.SAIDA, 1, new BigDecimal("20.00"), null
        );

        assertNotNull(mov.getDataVenda());
        assertNotNull(mov.getDataMovimento());
        assertEquals(9, produto.getEstoque());
    }

    @Test
    void lancarExcecaoQuandoSaidaSemSaldo() {
        Produto produto = produtoPadrao(3);
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
    void lancarExcecaoQuandoProdutoNaoExiste() {
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
    void lancarExcecaoQuandoSaidaSemValorVenda() {
        Produto produto = produtoPadrao(10);
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

    @Test
    void lancarExcecaoQuandoTipoNull() {
        Produto produto = produtoPadrao(10);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        EstoqueInvalidoException ex = assertThrows(
                EstoqueInvalidoException.class,
                () -> movimentoEstoqueService.registrarMovimento(
                        1L, null, 1, null, null
                )
        );

        assertEquals("Tipo de movimentação é obrigatório.", ex.getMessage());
        verify(produtoRepository, never()).save(any());
        verify(movimentoEstoqueRepository, never()).save(any());
    }

    @Test
    void lancarExcecaoQuantidadeInvalida() {
        Produto produto = produtoPadrao(10);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        EstoqueInvalidoException ex = assertThrows(
                EstoqueInvalidoException.class,
                () -> movimentoEstoqueService.registrarMovimento(
                        1L, TipoMovimentacao.ENTRADA, 0, null, null
                )
        );

        assertTrue(ex.getMessage().contains("Quantidade deve ser maior que zero"));
        verify(produtoRepository, never()).save(any());
        verify(movimentoEstoqueRepository, never()).save(any());
    }

    private Produto produtoPadrao(int estoque) {
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Produto X");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("50.00"));
        produto.setEstoque(estoque);
        return produto;
    }

    @Test
    void mapearDescricaoDoProdutoECodigoProdutoEEstoqueAtual() {
        Produto produto = new Produto();
        produto.setCodigo(10L);
        produto.setDescricao("Teclado Mecânico");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("220.00"));
        produto.setEstoque(15);

        MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
        movimentoEstoque.setCodigoMovimentacao(99L);
        movimentoEstoque.setProduto(produto);
        movimentoEstoque.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentoEstoque.setQtdMovimentada(2);
        movimentoEstoque.setValorVenda(new BigDecimal("299.90"));
        movimentoEstoque.setDataVenda(LocalDateTime.of(2026, 2, 22, 10, 0));
        movimentoEstoque.setDataMovimento(LocalDateTime.of(2026, 2, 22, 10, 5));

        MovimentoEstoqueResponse res = MovimentoEstoqueMapper.toResponse(movimentoEstoque);

        assertNotNull(res);
        assertEquals(99L, res.getCodigoMovimentacao());
        assertEquals(10L, res.getCodigoProduto());
        assertEquals("Teclado Mecânico", res.getDescricaoProduto());
        assertEquals(15, res.getEstoqueAtual());

        assertEquals(TipoMovimentacao.SAIDA, res.getTipoMovimentacao());
        assertEquals(2, res.getQtdMovimentada());
        assertEquals(new BigDecimal("299.90"), res.getValorVenda());
        assertEquals(LocalDateTime.of(2026, 2, 22, 10, 0), res.getDataVenda());
        assertEquals(LocalDateTime.of(2026, 2, 22, 10, 5), res.getDataMovimento());
    }

}