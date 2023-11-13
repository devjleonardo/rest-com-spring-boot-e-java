package com.joseleonardo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.data.dto.v1.security.CredenciaisDaContaDTO;
import com.joseleonardo.services.AutenticacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/autenticacao")
@Tag(name = "Autenticação", description = "Endpoints para autenticacão")
public class AutenticacaoController {

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Operation(summary = "Autentica um usuário e retorna um token")
	@PostMapping(value = "/signin")
	public ResponseEntity<?> signin(@RequestBody CredenciaisDaContaDTO credenciaisDaContaDTO) {
		if (verificarSeOsParametrosNaoSaoNulos(credenciaisDaContaDTO)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solicitação de cliente inválida");
		}
		
		ResponseEntity<?> token = autenticacaoService.signin(credenciaisDaContaDTO);
		
		if (token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solicitação de cliente inválida");
		}
		
		return token;
	}
	
	@Operation(summary = "Atualizar token para usuário autenticado e retornar um token")
	@PutMapping(value = "/refresh/{nomeDeUsuario}")
	public ResponseEntity<?> refreshToken(@PathVariable("nomeDeUsuario") String nomeDeUsuario,
			@RequestHeader("Authorization") String refreshToken) {
		if (verificarSeOsParametrosNaoSaoNulos(nomeDeUsuario, refreshToken)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solicitação de cliente inválida");
		}
		
		ResponseEntity<?> token = autenticacaoService.refreshToken(nomeDeUsuario, refreshToken);
		
		if (token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solicitação de cliente inválida");
		}
		
		return token;
	}

	private boolean verificarSeOsParametrosNaoSaoNulos(CredenciaisDaContaDTO credenciaisDaContaDTO) {
		return credenciaisDaContaDTO == null 
				|| credenciaisDaContaDTO.getNomeDeUsuario() == null 
				|| credenciaisDaContaDTO.getNomeDeUsuario().isBlank() 
				|| credenciaisDaContaDTO.getSenha() == null
				|| credenciaisDaContaDTO.getSenha().isBlank();
	}
	
	private boolean verificarSeOsParametrosNaoSaoNulos(String nomeDeUsuario, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() 
				|| nomeDeUsuario == null || nomeDeUsuario.isBlank();
	}
	
}
