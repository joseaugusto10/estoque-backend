package br.com.nexdom.estoque_backend.controllers;

import br.com.nexdom.estoque_backend.domain.entities.Produto;
import br.com.nexdom.estoque_backend.domain.enums.TipoProduto;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoCreateRequest;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoResponse;
import br.com.nexdom.estoque_backend.dtos.produto.ProdutoUpdateRequest;
import br.com.nexdom.estoque_backend.mappers.ProdutoMapper;
import br.com.nexdom.estoque_backend.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody ProdutoCreateRequest produtoCreateRequest) {
        Produto criado = produtoService.criar(ProdutoMapper.toEntity(produtoCreateRequest));
        return ProdutoMapper.toResponse(criado);
    }

    @GetMapping
    public Page<ProdutoResponse> listar(
            @PageableDefault(page = 0, size = 10, sort = "codigo", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) TipoProduto tipo
    ) {
        Page<Produto> produtos = (tipo == null)
                ? produtoService.listar(pageable)
                : produtoService.listarPorTipo(tipo, pageable);

        return produtos.map(ProdutoMapper::toResponse);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return ProdutoMapper.toResponse(produtoService.buscarPorId(id));
    }


    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id,
                                     @Valid @RequestBody ProdutoUpdateRequest req) {
        Produto atualizado = produtoService.atualizar(id, ProdutoMapper.toEntity(req));
        return ProdutoMapper.toResponse(atualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        produtoService.excluir(id);
    }
}