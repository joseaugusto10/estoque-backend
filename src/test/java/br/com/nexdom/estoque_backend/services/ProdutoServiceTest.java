package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.dtos.produto.LucroProdutoResponse;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoResumoResponse;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void criarProduto() {
        Produto produto = new Produto();
        produto.setDescricao("Mouse");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("50.00"));
        produto.setEstoque(10);

        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto criado = produtoService.criar(produto);

        assertNotNull(criado);
        assertEquals("Mouse", criado.getDescricao());

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(captor.capture());
        assertEquals("Mouse", captor.getValue().getDescricao());
    }

    @Test
    void listarProdutos() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("codigo").descending());
        when(produtoRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        Page<Produto> page = produtoService.listar(pageable);

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        verify(produtoRepository).findAll(pageable);
    }

    @Test
    void listarProdutosPorTipo() {
        Pageable pageable = PageRequest.of(0, 10);
        when(produtoRepository.findByTipoProduto(TipoProduto.ELETRONICO, pageable))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        Page<Produto> page = produtoService.listarPorTipo(TipoProduto.ELETRONICO, pageable);

        assertNotNull(page);
        verify(produtoRepository).findByTipoProduto(TipoProduto.ELETRONICO, pageable);
    }

    @Test
    void buscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Teclado");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto encontrado = produtoService.buscarPorId(1L);

        assertEquals("Teclado", encontrado.getDescricao());
        verify(produtoRepository).findById(1L);
    }

    @Test
    void atualizarProduto() {
        Produto existente = new Produto();
        existente.setCodigo(1L);
        existente.setDescricao("Antigo");
        existente.setTipoProduto(TipoProduto.MOVEL);
        existente.setValorNoFornecedor(new BigDecimal("10.00"));
        existente.setEstoque(5);

        Produto novo = new Produto();
        novo.setDescricao("Novo");
        novo.setTipoProduto(TipoProduto.ELETRONICO);
        novo.setValorNoFornecedor(new BigDecimal("99.90"));
        novo.setEstoque(7);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto atualizado = produtoService.atualizar(1L, novo);

        assertEquals("Novo", atualizado.getDescricao());
        assertEquals(TipoProduto.ELETRONICO, atualizado.getTipoProduto());
        assertEquals(new BigDecimal("99.90"), atualizado.getValorNoFornecedor());
        assertEquals(7, atualizado.getEstoque());

        verify(produtoRepository).save(existente);
    }

    @Test
    void excluirProduto() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.excluir(1L);

        verify(produtoRepository).deleteById(1L);
    }

    @Test
    void lancarExcecaoAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(80L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(80L)
        );

        assertTrue(ex.getMessage().contains("Produto não encontrado"));
        verify(produtoRepository).findById(80L);
    }

    @Test
    void listarResumoPorTipo() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProdutoResumoResponse> esperado = new PageImpl<>(List.of(), pageable, 0);

        when(produtoRepository.listarResumoPorTipo(eq(TipoProduto.ELETRONICO), eq(TipoMovimentacao.SAIDA), eq(pageable)))
                .thenReturn(esperado);

        Page<ProdutoResumoResponse> page = produtoService.listarResumoPorTipo(TipoProduto.ELETRONICO, pageable);

        assertNotNull(page);
        verify(produtoRepository).listarResumoPorTipo(TipoProduto.ELETRONICO, TipoMovimentacao.SAIDA, pageable);
    }

    @Test
    void consultarLucroPorProduto() {
        LucroProdutoResponse resp = new LucroProdutoResponse(
                1L, "Mouse", TipoProduto.ELETRONICO, 5L, new BigDecimal("100.00")
        );

        when(produtoRepository.consultarLucroPorProduto(1L, TipoMovimentacao.SAIDA))
                .thenReturn(Optional.of(resp));

        LucroProdutoResponse retorno = produtoService.consultarLucroPorProduto(1L);

        assertNotNull(retorno);
        assertEquals(1L, retorno.getCodigoProduto());
        verify(produtoRepository).consultarLucroPorProduto(1L, TipoMovimentacao.SAIDA);
    }

    @Test
    void lancarExcecaoAoConsultarLucroDeProdutoInexistente() {
        when(produtoRepository.consultarLucroPorProduto(80L, TipoMovimentacao.SAIDA))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.consultarLucroPorProduto(80L)
        );

        assertTrue(ex.getMessage().contains("Produto não encontrado"));
        verify(produtoRepository).consultarLucroPorProduto(80L, TipoMovimentacao.SAIDA);
    }
}