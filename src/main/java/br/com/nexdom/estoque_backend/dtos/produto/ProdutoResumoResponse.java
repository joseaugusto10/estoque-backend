package br.com.nexdom.estoque_backend.dtos.produto;

import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;

import java.math.BigDecimal;

public class ProdutoResumoResponse {

    private Long codigo;
    private String descricao;
    private TipoProduto tipoProduto;
    private BigDecimal valorNoFornecedor;
    private Integer estoqueDisponivel;
    private Long quantidadeTotalSaida;

    public ProdutoResumoResponse() {
    }

    public ProdutoResumoResponse(Long codigo,
                                 String descricao,
                                 TipoProduto tipoProduto,
                                 BigDecimal valorNoFornecedor,
                                 Integer estoqueDisponivel,
                                 Long quantidadeTotalSaida) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.tipoProduto = tipoProduto;
        this.valorNoFornecedor = valorNoFornecedor;
        this.estoqueDisponivel = estoqueDisponivel;
        this.quantidadeTotalSaida = quantidadeTotalSaida;
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

    public Integer getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    public void setEstoqueDisponivel(Integer estoqueDisponivel) {
        this.estoqueDisponivel = estoqueDisponivel;
    }

    public Long getQuantidadeTotalSaida() {
        return quantidadeTotalSaida;
    }

    public void setQuantidadeTotalSaida(Long quantidadeTotalSaida) {
        this.quantidadeTotalSaida = quantidadeTotalSaida;
    }
}
