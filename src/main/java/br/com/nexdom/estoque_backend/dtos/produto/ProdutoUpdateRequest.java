package br.com.nexdom.estoque_backend.dtos.produto;

import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoUpdateRequest {

    @NotBlank
    private String descricao;

    @NotNull
    private TipoProduto tipoProduto;

    @NotNull
    private BigDecimal valorNoFornecedor;

    @NotNull
    private Integer estoque;

    public ProdutoUpdateRequest() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public BigDecimal getValorNoFornecedor() {
        return valorNoFornecedor;
    }

    public void setValorNoFornecedor(BigDecimal valorNoFornecedor) {
        this.valorNoFornecedor = valorNoFornecedor;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
