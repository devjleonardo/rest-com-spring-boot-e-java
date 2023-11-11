package com.joseleonardo.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joseleonardo.model.Usuario;
import com.joseleonardo.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	private Logger logger = Logger.getLogger(UsuarioService.class.getName());
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String nomeDeUsuario) throws UsernameNotFoundException {
		logger.info("Encontrar uma pessoa por nome " + nomeDeUsuario + "!");
		
		Usuario usuario = usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);
		
		if (usuario != null) {
			return usuario;
		} else {
			throw new UsernameNotFoundException("Nome de usuário " + nomeDeUsuario + " não encontrado!");
		}
	}
	
}
