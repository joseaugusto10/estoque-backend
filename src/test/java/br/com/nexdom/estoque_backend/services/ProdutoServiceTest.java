package br.com.nexdom.estoque_backend.services;



import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

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

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto criado = produtoService.criar(produto);

        assertNotNull(criado);
        assertEquals("Mouse", criado.getDescricao());
        verify(produtoRepository).save(produto);
    }

    @Test
    void listarProdutos() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("codigo").descending());
        when(produtoRepository.findAll(pageable)).thenReturn(Page.empty(pageable));

        Page<Produto> page = produtoService.listar(pageable);

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        verify(produtoRepository).findAll(pageable);
    }

    @Test
    void buscarPorId() {
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
        when(produtoRepository.save(any(Produto.class))).thenReturn(existente);

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
    void lancarExcecaoQuandoNaoEncontrar() {
        when(produtoRepository.findById(80L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(80L)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("produto"));
        verify(produtoRepository).findById(80L);
    }
}
