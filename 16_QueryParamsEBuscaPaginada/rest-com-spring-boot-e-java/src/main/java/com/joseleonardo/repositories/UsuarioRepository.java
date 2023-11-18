package com.joseleonardo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joseleonardo.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.nomeDeUsuario =:nomeDeUsuario")
	Usuario buscarPorNomeDeUsuario(@Param("nomeDeUsuario") String nomeDeUsuario);
	
}
