package com.joseleonardo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.model.Pessoa;
import com.joseleonardo.services.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;
	// private PessoaService pessoaService = new PessoaService();
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Pessoa> listar() {
		return pessoaService.listarTodas();
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Pessoa buscarPorId(@PathVariable(value = "id") String id) {
		return pessoaService.buscarPorId(id);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Pessoa salvar(@RequestBody Pessoa pessoa) {
		return pessoaService.salvar(pessoa);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Pessoa atualizar(@RequestBody Pessoa pessoa) {
		return pessoaService.atualizar(pessoa);
	}
	
	@DeleteMapping(value = "/{id}")
	public void deletar(@PathVariable(value = "id") String id) {
		pessoaService.deletar(id);
	}
	
}
