package br.com.nexdom.estoque_backend.controllers;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import br.com.nexdom.estoque_backend.dtos.movimento.MovimentoEstoqueCreateRequest;
import br.com.nexdom.estoque_backend.dtos.movimento.MovimentoEstoqueResponse;
import br.com.nexdom.estoque_backend.mappers.MovimentoEstoqueMapper;
import br.com.nexdom.estoque_backend.services.MovimentoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimentos")
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoEstoqueService;

    public MovimentoEstoqueController(MovimentoEstoqueService movimentoEstoqueService) {
        this.movimentoEstoqueService = movimentoEstoqueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentoEstoqueResponse criar(@Valid @RequestBody MovimentoEstoqueCreateRequest req) {
        MovimentoEstoque movimentoEstoque = movimentoEstoqueService.registrarMovimento(
                req.getCodigoProduto(),
                req.getTipoMovimentacao(),
                req.getQtdMovimentada(),
                req.getValorVenda(),
                req.getDataVenda()
        );
        return MovimentoEstoqueMapper.toResponse(movimentoEstoque);
    }

    @GetMapping
    public Page<MovimentoEstoqueResponse> listar(
            @PageableDefault(page = 0, size = 10, sort = "codigoMovimentacao", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return movimentoEstoqueService.listar(pageable).map(MovimentoEstoqueMapper::toResponse);
    }

    @GetMapping("/{id}")
    public MovimentoEstoqueResponse buscar(@PathVariable Long id) {
        MovimentoEstoque movimentoEstoque = movimentoEstoqueService.buscarPorId(id);
        return MovimentoEstoqueMapper.toResponse(movimentoEstoque);
    }

}
