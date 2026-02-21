package br.com.nexdom.estoque_backend.domain.entities;

import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produto", nullable = false)
    private TipoProduto tipoProduto;

    @Column(name = "valor_no_fornecedor", nullable = false)
    private BigDecimal valorNoFornecedor;

    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    public Produto() {}

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