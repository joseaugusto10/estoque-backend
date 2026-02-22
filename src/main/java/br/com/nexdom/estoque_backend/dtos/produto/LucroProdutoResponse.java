package br.com.nexdom.estoque_backend.dtos.produto;

import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;

import java.math.BigDecimal;

public class LucroProdutoResponse {

    private Long codigoProduto;
    private String descricao;
    private TipoProduto tipoProduto;
    private Long quantidadeTotalSaida;
    private BigDecimal lucroTotal;

    public LucroProdutoResponse() {
    }

    public LucroProdutoResponse(Long codigoProduto,
                                String descricao,
                                TipoProduto tipoProduto,
                                Long quantidadeTotalSaida,
                                BigDecimal lucroTotal) {
        this.codigoProduto = codigoProduto;
        this.descricao = descricao;
        this.tipoProduto = tipoProduto;
        this.quantidadeTotalSaida = quantidadeTotalSaida;
        this.lucroTotal = lucroTotal;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
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

    public Long getQuantidadeTotalSaida() {
        return quantidadeTotalSaida;
    }

    public void setQuantidadeTotalSaida(Long quantidadeTotalSaida) {
        this.quantidadeTotalSaida = quantidadeTotalSaida;
    }

    public BigDecimal getLucroTotal() {
        return lucroTotal;
    }

    public void setLucroTotal(BigDecimal lucroTotal) {
        this.lucroTotal = lucroTotal;
    }
}
