package com.joseleonardo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joseleonardo.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Modifying
	@Query("UPDATE Pessoa p SET p.habilitada = false WHERE p.id =:id")
	void desabilitarPessoa(@Param("id") Long id);
	
	@Query("SELECT p FROM Pessoa p WHERE p.primeiroNome LIKE LOWER(CONCAT ('%',:primeiroNome,'%'))")
	Page<Pessoa> buscarPessoasPorNome(@Param("primeiroNome") String primeiroNome, Pageable pageable);
	
}
