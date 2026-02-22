package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.exceptions.EstoqueInvalidoException;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.repositories.MovimentoEstoqueRepository;
import br.com.nexdom.estoque_backend.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MovimentoEstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;

    public MovimentoEstoqueService(ProdutoRepository produtoRepository,
                                   MovimentoEstoqueRepository movimentoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentoEstoqueRepository = movimentoEstoqueRepository;
    }

    public MovimentoEstoque registrarMovimento(Long codigoProduto,
                                               TipoMovimentacao tipoMovimentacao,
                                               Integer qtdMovimentada,
                                               BigDecimal valorVenda,
                                               LocalDateTime dataVenda) {

        if (codigoProduto == null) {
            throw new EstoqueInvalidoException("Código do produto é obrigatório.");
        }

        Produto produto = produtoRepository.findById(codigoProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + codigoProduto));

        validarParametros(produto.getEstoque(), qtdMovimentada, tipoMovimentacao);
        LocalDateTime dataMovimento = LocalDateTime.now();


        if (tipoMovimentacao == TipoMovimentacao.SAIDA) {
            if (valorVenda == null) {
                throw new EstoqueInvalidoException("Valor de venda é obrigatório para saída.");
            }
            if (dataVenda == null) {
                dataVenda = LocalDateTime.now();
            }
        } else {
            // entrada não tem venda
            valorVenda = null;
            dataVenda = null;
        }

        int novoEstoque = calcularNovoEstoque(produto.getEstoque(), qtdMovimentada, tipoMovimentacao);

        produto.setEstoque(novoEstoque);
        Produto produtoSalvo = produtoRepository.save(produto);

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setProduto(produtoSalvo);
        movimento.setTipoMovimentacao(tipoMovimentacao);
        movimento.setQtdMovimentada(qtdMovimentada);
        movimento.setValorVenda(valorVenda);
        movimento.setDataVenda(dataVenda);
        movimento.setDataMovimento(dataMovimento);

        return movimentoEstoqueRepository.save(movimento);
    }

    private void validarParametros(int estoqueAtual, Integer qtd, TipoMovimentacao tipo) {
        if (tipo == null) {
            throw new EstoqueInvalidoException("Tipo de movimentação é obrigatório.");
        }
        if (estoqueAtual < 0) {
            throw new EstoqueInvalidoException("Estoque atual não pode ser negativo: " + estoqueAtual);
        }
        if (qtd == null || qtd <= 0) {
            throw new EstoqueInvalidoException("Quantidade deve ser maior que zero. Informado: " + qtd);
        }
    }

    private int calcularNovoEstoque(int estoqueAtual, int qtd, TipoMovimentacao tipo) {
        return switch (tipo) {
            case ENTRADA -> estoqueAtual + qtd;
            case SAIDA -> {
                validarSaldoParaSaida(estoqueAtual, qtd);
                yield estoqueAtual - qtd;
            }
        };
    }

    private void validarSaldoParaSaida(int estoqueAtual, int qtd) {
        if (estoqueAtual < qtd) {
            throw new EstoqueInvalidoException(
                    "Saldo insuficiente. Estoque atual: " + estoqueAtual + ", Saida solicitada: " + qtd
            );
        }
    }

}
