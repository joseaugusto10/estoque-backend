package br.com.nexdom.estoque_backend.controllers;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.exceptions.EstoqueInvalidoException;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.services.MovimentoEstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimentoEstoqueController.class)
class MovimentoEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovimentoEstoqueService movimentoEstoqueService;

    @Test
    void criarEntrada() throws Exception {

        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Mouse");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("50.00"));
        produto.setEstoque(15);

        MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
        movimentoEstoque.setCodigoMovimentacao(10L);
        movimentoEstoque.setProduto(produto);
        movimentoEstoque.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentoEstoque.setQtdMovimentada(5);
        movimentoEstoque.setValorVenda(null);
        movimentoEstoque.setDataVenda(null);
        movimentoEstoque.setDataMovimento(LocalDateTime.of(2026, 2, 22, 10, 0));

        when(movimentoEstoqueService.registrarMovimento(
                eq(1L), eq(TipoMovimentacao.ENTRADA), eq(5), isNull(), isNull()
        )).thenReturn(movimentoEstoque);

        String payload = """
            {
              "codigoProduto": 1,
              "tipoMovimentacao": "ENTRADA",
              "qtdMovimentada": 5
            }
        """;

        mockMvc.perform(post("/movimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoMovimentacao").value(10))
                .andExpect(jsonPath("$.codigoProduto").value(1))
                .andExpect(jsonPath("$.tipoMovimentacao").value("ENTRADA"))
                .andExpect(jsonPath("$.qtdMovimentada").value(5))
                .andExpect(jsonPath("$.valorVenda").value(nullValue()))
                .andExpect(jsonPath("$.dataVenda").value(nullValue()))
                .andExpect(jsonPath("$.dataMovimento").exists())
                .andExpect(jsonPath("$.estoqueAtual").value(15));
    }

    @Test
    void criarSaida() throws Exception {

        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Teclado");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("30.00"));
        produto.setEstoque(6);

        MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
        movimentoEstoque.setCodigoMovimentacao(11L);
        movimentoEstoque.setProduto(produto);
        movimentoEstoque.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentoEstoque.setQtdMovimentada(4);
        movimentoEstoque.setValorVenda(new BigDecimal("100.00"));
        movimentoEstoque.setDataVenda(LocalDateTime.of(2026, 2, 22, 12, 0));
        movimentoEstoque.setDataMovimento(LocalDateTime.of(2026, 2, 22, 12, 0));

        when(movimentoEstoqueService.registrarMovimento(
                eq(1L),
                eq(TipoMovimentacao.SAIDA),
                eq(4),
                eq(new BigDecimal("100.00")),
                eq(LocalDateTime.of(2026, 2, 22, 12, 0))
        )).thenReturn(movimentoEstoque);

        String payload = """
            {
              "codigoProduto": 1,
              "tipoMovimentacao": "SAIDA",
              "qtdMovimentada": 4,
              "valorVenda": 100.00,
              "dataVenda": "2026-02-22T12:00:00"
            }
        """;

        mockMvc.perform(post("/movimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoMovimentacao").value(11))
                .andExpect(jsonPath("$.codigoProduto").value(1))
                .andExpect(jsonPath("$.tipoMovimentacao").value("SAIDA"))
                .andExpect(jsonPath("$.qtdMovimentada").value(4))
                .andExpect(jsonPath("$.valorVenda").value(100.00))
                .andExpect(jsonPath("$.dataVenda").value("2026-02-22T12:00:00"))
                .andExpect(jsonPath("$.dataMovimento").value("2026-02-22T12:00:00"))
                .andExpect(jsonPath("$.estoqueAtual").value(6));
    }

    @Test
    void listar() throws Exception {

        Produto produto = new Produto();
        produto.setCodigo(2L);
        produto.setDescricao("Cadeira");
        produto.setTipoProduto(TipoProduto.MOVEL);
        produto.setValorNoFornecedor(new BigDecimal("80.00"));
        produto.setEstoque(9);

        MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
        movimentoEstoque.setCodigoMovimentacao(20L);
        movimentoEstoque.setProduto(produto);
        movimentoEstoque.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentoEstoque.setQtdMovimentada(1);
        movimentoEstoque.setDataMovimento(LocalDateTime.of(2026, 2, 22, 13, 0));

        PageRequest pageable = PageRequest.of(0, 10);
        Page<MovimentoEstoque> page = new PageImpl<>(List.of(movimentoEstoque), pageable, 1);

        when(movimentoEstoqueService.listar(any())).thenReturn(page);

        mockMvc.perform(get("/movimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigoMovimentacao").value(20))
                .andExpect(jsonPath("$.content[0].codigoProduto").value(2))
                .andExpect(jsonPath("$.content[0].tipoMovimentacao").value("ENTRADA"))
                .andExpect(jsonPath("$.content[0].qtdMovimentada").value(1))
                .andExpect(jsonPath("$.content[0].dataMovimento").value("2026-02-22T13:00:00"))
                .andExpect(jsonPath("$.content[0].estoqueAtual").value(9));
    }

    @Test
    void buscar() throws Exception {

        Produto produto = new Produto();
        produto.setCodigo(3L);
        produto.setDescricao("Monitor");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("500.00"));
        produto.setEstoque(2);

        MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
        movimentoEstoque.setCodigoMovimentacao(30L);
        movimentoEstoque.setProduto(produto);
        movimentoEstoque.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentoEstoque.setQtdMovimentada(1);
        movimentoEstoque.setValorVenda(new BigDecimal("900.00"));
        movimentoEstoque.setDataVenda(LocalDateTime.of(2026, 2, 22, 14, 0));
        movimentoEstoque.setDataMovimento(LocalDateTime.of(2026, 2, 22, 14, 0));

        when(movimentoEstoqueService.buscarPorId(30L)).thenReturn(movimentoEstoque);

        mockMvc.perform(get("/movimentos/30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoMovimentacao").value(30))
                .andExpect(jsonPath("$.codigoProduto").value(3))
                .andExpect(jsonPath("$.tipoMovimentacao").value("SAIDA"))
                .andExpect(jsonPath("$.qtdMovimentada").value(1))
                .andExpect(jsonPath("$.valorVenda").value(900.00))
                .andExpect(jsonPath("$.dataVenda").value("2026-02-22T14:00:00"))
                .andExpect(jsonPath("$.dataMovimento").value("2026-02-22T14:00:00"))
                .andExpect(jsonPath("$.estoqueAtual").value(2));
    }

    @Test
    void buscarInexistente() throws Exception {

        when(movimentoEstoqueService.buscarPorId(80L))
                .thenThrow(new RecursoNaoEncontradoException("Movimento não encontrado: 80"));

        mockMvc.perform(get("/movimentos/80"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarSaidaSemSaldo() throws Exception {

        when(movimentoEstoqueService.registrarMovimento(
                eq(1L),
                eq(TipoMovimentacao.SAIDA),
                eq(10),
                eq(new BigDecimal("900.00")),
                eq(LocalDateTime.of(2026, 2, 22, 12, 0))
        )).thenThrow(new EstoqueInvalidoException("Saldo insuficiente. Estoque atual: 3, Saida solicitada: 10"));

        String payload = """
            {
              "codigoProduto": 1,
              "tipoMovimentacao": "SAIDA",
              "qtdMovimentada": 10,
              "valorVenda": 900.00,
              "dataVenda": "2026-02-22T12:00:00"
            }
        """;

        mockMvc.perform(post("/movimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_CONTENT.value()));
    }

    @Test
    void criarMovimentoProdutoInexistente() throws Exception {

        when(movimentoEstoqueService.registrarMovimento(
                eq(80L),
                eq(TipoMovimentacao.ENTRADA),
                eq(1),
                isNull(),
                isNull()
        )).thenThrow(new RecursoNaoEncontradoException("Produto não encontrado: 80"));

        String payload = """
            {
              "codigoProduto": 80,
              "tipoMovimentacao": "ENTRADA",
              "qtdMovimentada": 1
            }
        """;

        mockMvc.perform(post("/movimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());
    }
}