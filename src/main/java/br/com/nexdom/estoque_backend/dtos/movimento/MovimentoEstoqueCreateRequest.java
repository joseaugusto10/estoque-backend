package br.com.nexdom.estoque_backend.dtos.movimento;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimentoEstoqueCreateRequest {

    @NotNull
    private Long codigoProduto;

    @NotNull
    private TipoMovimentacao tipoMovimentacao;

    @NotNull
    private Integer qtdMovimentada;
    private BigDecimal valorVenda;
    private LocalDateTime dataVenda;

    public MovimentoEstoqueCreateRequest() {
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
}
