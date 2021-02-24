package br.com.adev.appfinancas.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.adev.appfinancas.model.Conta;

public interface ContaRepository extends CrudRepository<Conta, String> {
    Conta findByIdConta(long idConta);
}
