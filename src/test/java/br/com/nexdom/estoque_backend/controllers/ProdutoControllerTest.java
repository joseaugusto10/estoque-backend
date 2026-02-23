package br.com.nexdom.estoque_backend.controllers;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.dtos.produto.LucroProdutoResponse;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoResumoResponse;
import br.com.nexdom.estoque_backend.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void criarProduto() throws Exception {
        Produto salvo = new Produto();
        salvo.setCodigo(1L);
        salvo.setDescricao("Mouse");
        salvo.setTipoProduto(TipoProduto.ELETRONICO);
        salvo.setValorNoFornecedor(new BigDecimal("50.00"));
        salvo.setEstoque(10);

        when(produtoService.criar(any())).thenReturn(salvo);

        String payload = """
            {
              "descricao": "Mouse",
              "tipoProduto": "ELETRONICO",
              "valorNoFornecedor": 50.00,
              "estoque": 10
            }
        """;

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value(1))
                .andExpect(jsonPath("$.descricao").value("Mouse"))
                .andExpect(jsonPath("$.tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.valorNoFornecedor").value(50.00))
                .andExpect(jsonPath("$.estoque").value(10));
    }

    @Test
    void listarProdutos() throws Exception {
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Teclado");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("30.00"));
        produto.setEstoque(5);

        Page<Produto> page = new PageImpl<>(List.of(produto), PageRequest.of(0, 10), 1);
        when(produtoService.listar(any())).thenReturn(page);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigo").value(1))
                .andExpect(jsonPath("$.content[0].descricao").value("Teclado"))
                .andExpect(jsonPath("$.content[0].tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.content[0].valorNoFornecedor").value(30.00))
                .andExpect(jsonPath("$.content[0].estoque").value(5));
    }

    @Test
    void listarProdutosPorTipo() throws Exception {
        Produto p = new Produto();
        p.setCodigo(2L);
        p.setDescricao("Mousepad");
        p.setTipoProduto(TipoProduto.ELETRONICO);
        p.setValorNoFornecedor(new BigDecimal("20.00"));
        p.setEstoque(15);

        Page<Produto> page = new PageImpl<>(List.of(p), PageRequest.of(0, 10), 1);
        when(produtoService.listarPorTipo(eq(TipoProduto.ELETRONICO), any())).thenReturn(page);

        mockMvc.perform(get("/produtos").param("tipo", "ELETRONICO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigo").value(2))
                .andExpect(jsonPath("$.content[0].descricao").value("Mousepad"))
                .andExpect(jsonPath("$.content[0].tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.content[0].valorNoFornecedor").value(20.00))
                .andExpect(jsonPath("$.content[0].estoque").value(15));
    }

    @Test
    void buscarProdutoPorId() throws Exception {
        Produto produto = new Produto();
        produto.setCodigo(1L);
        produto.setDescricao("Monitor");
        produto.setTipoProduto(TipoProduto.ELETRONICO);
        produto.setValorNoFornecedor(new BigDecimal("500.00"));
        produto.setEstoque(2);

        when(produtoService.buscarPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(1))
                .andExpect(jsonPath("$.descricao").value("Monitor"))
                .andExpect(jsonPath("$.tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.valorNoFornecedor").value(500.00))
                .andExpect(jsonPath("$.estoque").value(2));
    }

    @Test
    void atualizarProduto() throws Exception {
        Produto atualizado = new Produto();
        atualizado.setCodigo(1L);
        atualizado.setDescricao("Monitor 27");
        atualizado.setTipoProduto(TipoProduto.ELETRONICO);
        atualizado.setValorNoFornecedor(new BigDecimal("999.90"));
        atualizado.setEstoque(2);

        when(produtoService.atualizar(eq(1L), any())).thenReturn(atualizado);

        String payload = """
            {
              "descricao": "Monitor 27",
              "tipoProduto": "ELETRONICO",
              "valorNoFornecedor": 999.90,
              "estoque": 2
            }
        """;

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(1))
                .andExpect(jsonPath("$.descricao").value("Monitor 27"))
                .andExpect(jsonPath("$.tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.valorNoFornecedor").value(999.90))
                .andExpect(jsonPath("$.estoque").value(2));
    }

    @Test
    void excluirProduto() throws Exception {
        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarResumo() throws Exception {
        ProdutoResumoResponse resumo = new ProdutoResumoResponse(
                1L,
                "Mouse Gamer",
                TipoProduto.ELETRONICO,
                new BigDecimal("150.00"),
                20,
                7L
        );

        Page<ProdutoResumoResponse> page = new PageImpl<>(
                List.of(resumo),
                PageRequest.of(0, 10),
                1
        );

        when(produtoService.listarResumoPorTipo(eq(TipoProduto.ELETRONICO), any()))
                .thenReturn(page);

        mockMvc.perform(get("/produtos/resumo")
                        .param("tipo", "ELETRONICO")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "codigo,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigo").value(1))
                .andExpect(jsonPath("$.content[0].descricao").value("Mouse Gamer"))
                .andExpect(jsonPath("$.content[0].tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.content[0].valorNoFornecedor").value(150.00))
                .andExpect(jsonPath("$.content[0].estoqueDisponivel").value(20))
                .andExpect(jsonPath("$.content[0].quantidadeTotalSaida").value(7));
    }

    @Test
    void consultarLucro() throws Exception {
        LucroProdutoResponse resp = new LucroProdutoResponse(
                1L,
                "Mouse Gamer",
                TipoProduto.ELETRONICO,
                7L,
                new BigDecimal("350.00")
        );

        when(produtoService.consultarLucroPorProduto(1L)).thenReturn(resp);

        mockMvc.perform(get("/produtos/1/lucro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoProduto").value(1))
                .andExpect(jsonPath("$.descricao").value("Mouse Gamer"))
                .andExpect(jsonPath("$.tipoProduto").value("ELETRONICO"))
                .andExpect(jsonPath("$.quantidadeTotalSaida").value(7))
                .andExpect(jsonPath("$.lucroTotal").value(350.00));
    }

    @Test
    void excluirProdutoForcado() throws Exception {
        mockMvc.perform(delete("/produtos/1/forcar"))
                .andExpect(status().isNoContent());

        verify(produtoService).excluirComMovimentacoes(1L);
    }
}