package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoMovimentacao;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.dtos.produto.LucroProdutoResponse;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoResumoResponse;
import br.com.nexdom.estoque_backend.exceptions.RecursoDuplicadoException;
import br.com.nexdom.estoque_backend.exceptions.RecursoEmUsoException;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.repositories.MovimentoEstoqueRepository;
import br.com.nexdom.estoque_backend.repositories.ProdutoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;

    public ProdutoService(ProdutoRepository produtoRepository, MovimentoEstoqueRepository movimentoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentoEstoqueRepository = movimentoEstoqueRepository;
    }

    @Transactional
    public Produto criar(Produto produto) {
        if (produto.getDescricao() != null &&
                produtoRepository.existsByDescricaoIgnoreCase(produto.getDescricao())) {

            throw new RecursoDuplicadoException(
                    "Já existe um produto com a descrição: " + produto.getDescricao()
            );
        }
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Page<Produto> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Produto> listarPorTipo(TipoProduto tipo, Pageable pageable) {
        return produtoRepository.findByTipoProduto(tipo, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Produto> listarPorDescricao(String descricao, Pageable pageable) {
        if (descricao == null || descricao.trim().isEmpty()) {
            return produtoRepository.findAll(pageable);
        }
        return produtoRepository.findByDescricaoContainingIgnoreCase(descricao.trim(), pageable);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    @Transactional
    public Produto atualizar(Long id, Produto dados) {
        Produto existente = buscarPorId(id);

        if (dados.getDescricao() != null &&
                !dados.getDescricao().equalsIgnoreCase(existente.getDescricao()) &&
                produtoRepository.existsByDescricaoIgnoreCase(dados.getDescricao())) {

            throw new RecursoDuplicadoException(
                    "Já existe um produto com a descrição: " + dados.getDescricao()
            );
        }
        existente.setDescricao(dados.getDescricao());
        existente.setTipoProduto(dados.getTipoProduto());
        existente.setValorNoFornecedor(dados.getValorNoFornecedor());
        existente.setEstoque(dados.getEstoque());

        return produtoRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado: " + id);
        }

        boolean temMovimentacoes = movimentoEstoqueRepository.existsByProdutoCodigo(id);
        if (temMovimentacoes) {
            throw new RecursoEmUsoException(
                    "Não é possível excluir o produto porque existem movimentações vinculadas."
            );
        }
        produtoRepository.deleteById(id);
    }

    @Transactional
    public void excluirComMovimentacoes(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado: " + id);
        }
        movimentoEstoqueRepository.deleteByProdutoCodigo(id);
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResumoResponse> listarResumoPorTipo(TipoProduto tipo, Pageable pageable) {
        return produtoRepository.listarResumoPorTipo(tipo, TipoMovimentacao.SAIDA, pageable);
    }

    @Transactional(readOnly = true)
    public LucroProdutoResponse consultarLucroPorProduto(Long codigoProduto) {
        return produtoRepository.consultarLucroPorProduto(codigoProduto, TipoMovimentacao.SAIDA)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + codigoProduto));
    }


}