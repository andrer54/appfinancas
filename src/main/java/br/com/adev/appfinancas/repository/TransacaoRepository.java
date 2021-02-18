package br.com.adev.appfinancas.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.adev.appfinancas.model.Transacao;

public interface TransacaoRepository extends CrudRepository<Transacao, String>{
    Transacao findByIdTransacao(long idTransacao);   
    
}
