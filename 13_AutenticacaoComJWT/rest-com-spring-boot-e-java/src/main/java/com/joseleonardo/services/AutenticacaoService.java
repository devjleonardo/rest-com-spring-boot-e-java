package com.joseleonardo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joseleonardo.data.dto.v1.security.CredenciaisDaContaDTO;
import com.joseleonardo.data.dto.v1.security.TokenDTO;
import com.joseleonardo.model.Usuario;
import com.joseleonardo.repositories.UsuarioRepository;
import com.joseleonardo.security.jwt.JwtTokenProvider;

@Service
public class AutenticacaoService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public ResponseEntity<?> entrar(CredenciaisDaContaDTO credenciaisDaContaDTO) {
		try {
			String nomeDeUsuario = credenciaisDaContaDTO.getNomeDeUsuario();
			String senha = credenciaisDaContaDTO.getSenha();
			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(nomeDeUsuario, senha));
			
			Usuario usuario = usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);
			
			TokenDTO tokenResponse = new TokenDTO();
			
			if(usuario != null) {
				tokenResponse = jwtTokenProvider.criarAccessToken(nomeDeUsuario, usuario.getRoles());
			} else {
				throw new UsernameNotFoundException("Nome de usuário " + nomeDeUsuario + 
						" não encontrado!");
			}
			
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Nome de usuário/senha inválido!");
		}
	}
}
