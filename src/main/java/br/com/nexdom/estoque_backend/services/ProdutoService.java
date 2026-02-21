package br.com.nexdom.estoque_backend.services;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.exceptions.RecursoNaoEncontradoException;
import br.com.nexdom.estoque_backend.repositories.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto criar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Page<Produto> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto existente = buscarPorId(id);

        existente.setDescricao(dados.getDescricao());
        existente.setTipoProduto(dados.getTipoProduto());
        existente.setValorNoFornecedor(dados.getValorNoFornecedor());
        existente.setEstoque(dados.getEstoque());

        return produtoRepository.save(existente);
    }

    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado: " + id);
        }
        produtoRepository.deleteById(id);
    }
}
