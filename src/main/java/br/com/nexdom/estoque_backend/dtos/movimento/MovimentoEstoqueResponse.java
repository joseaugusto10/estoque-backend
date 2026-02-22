package br.com.nexdom.estoque_backend.dtos.movimento;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimentoEstoqueResponse {

    private Long codigoMovimentacao;
    private Long codigoProduto;
    private String descricaoProduto;
    private TipoMovimentacao tipoMovimentacao;
    private Integer qtdMovimentada;
    private BigDecimal valorVenda;
    private LocalDateTime dataVenda;
    private LocalDateTime dataMovimento;
    private Integer estoqueAtual;

    public MovimentoEstoqueResponse() {
    }

    public Long getCodigoMovimentacao() {
        return codigoMovimentacao;
    }

    public void setCodigoMovimentacao(Long codigoMovimentacao) {
        this.codigoMovimentacao = codigoMovimentacao;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public Integer getQtdMovimentada() {
        return qtdMovimentada;
    }

    public void setQtdMovimentada(Integer qtdMovimentada) {
        this.qtdMovimentada = qtdMovimentada;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public LocalDateTime getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDateTime dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public Integer getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Integer estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }
}
