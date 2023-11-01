package com.joseleonardo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.data.dto.v2.PessoaDTOV2;
import com.joseleonardo.services.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PessoaDTO> listar() {
		return pessoaService.listarTodas();
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PessoaDTO buscarPorId(@PathVariable(value = "id") Long id) {
		return pessoaService.buscarPorId(id);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PessoaDTO salvar(@RequestBody PessoaDTO pessoa) {
		return pessoaService.salvar(pessoa);
	}
	
	@PostMapping(value = "/v2", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public PessoaDTOV2 salvarV2(@RequestBody PessoaDTOV2 pessoa) {
		return pessoaService.salvarV2(pessoa);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PessoaDTO atualizar(@RequestBody PessoaDTO pessoa) {
		return pessoaService.atualizar(pessoa);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Long id) {
		pessoaService.deletar(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
