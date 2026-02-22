package br.com.nexdom.estoque_backend.dtos.produto;

import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;

import java.math.BigDecimal;

public class ProdutoResponse {

    private Long codigo;
    private String descricao;
    private TipoProduto tipoProduto;
    private BigDecimal valorNoFornecedor;
    private Integer estoque;

    public ProdutoResponse() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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
