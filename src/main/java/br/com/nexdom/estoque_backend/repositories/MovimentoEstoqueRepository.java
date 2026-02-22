package br.com.nexdom.estoque_backend.repositories;

import br.com.nexdom.estoque_backend.domain.entities.MovimentoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {
}