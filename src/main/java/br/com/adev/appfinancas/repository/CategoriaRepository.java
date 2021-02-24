package br.com.adev.appfinancas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.adev.appfinancas.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String>{
    Categoria findById(long id);
}
