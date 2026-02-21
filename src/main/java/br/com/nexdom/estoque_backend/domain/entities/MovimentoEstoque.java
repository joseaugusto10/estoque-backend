package br.com.nexdom.estoque_backend.domain.entities;

import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimento_estoque")
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_movimentacao")
    private Long codigoMovimentacao;

    @ManyToOne
    @JoinColumn(name = "codigo_produto", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "valor_venda")
    private BigDecimal valorVenda;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @Column(name = "qtd_movimentada", nullable = false)
    private Integer qtdMovimentada;

    public MovimentoEstoque() {}

    public Long getCodigoMovimentacao() {
        return codigoMovimentacao;
    }

    public void setCodigoMovimentacao(Long codigoMovimentacao) {
        this.codigoMovimentacao = codigoMovimentacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
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

    public Integer getQtdMovimentada() {
        return qtdMovimentada;
    }

    public void setQtdMovimentada(Integer qtdMovimentada) {
        this.qtdMovimentada = qtdMovimentada;
    }
}
