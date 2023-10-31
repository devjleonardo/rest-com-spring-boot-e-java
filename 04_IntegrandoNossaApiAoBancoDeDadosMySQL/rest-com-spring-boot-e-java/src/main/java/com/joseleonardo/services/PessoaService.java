package com.joseleonardo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.joseleonardo.model.Pessoa;

@Service
public class PessoaService {

	private final AtomicLong contador = new AtomicLong();
	private Logger logger = Logger.getLogger(PessoaService.class.getName());
	
	public List<Pessoa> listarTodas() {
		logger.info("Encontrar todas pessoas!");
		
		List<Pessoa> pessoas = new ArrayList<>();
		
		for (int i = 0; i < 8; i++) {
			Pessoa pessoa = mockPessoa(i);
			pessoas.add(pessoa);
		}
		
		return pessoas;
	}
	
	public Pessoa buscarPorId(String id) {
		logger.info("Encontrar uma pessoa!");
		
		Pessoa pessoa = new Pessoa();
		pessoa.setId(contador.incrementAndGet());
		pessoa.setPrimeiroNome("Yuri");
		pessoa.setUltimoNome("Alexandre");
		pessoa.setEndereco("Uberlândia - Minas Gerais - Brasil");
		pessoa.setGenero("Masculino");
		
		return pessoa;
	}
	
	public Pessoa salvar(Pessoa pessoa) {
		logger.info("Criando uma pessoa!");
		
		return pessoa;
	}
	
	public Pessoa atualizar(Pessoa pessoa) {
		logger.info("Atualizando uma pessoa!");
		
		return pessoa;
	}
	
	public void deletar(String id) {
		logger.info("Deletando uma pessoa!");
	}
	
	private Pessoa mockPessoa(int i) {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(contador.incrementAndGet());
		pessoa.setPrimeiroNome("Primeiro nome da pessoa " + i);
		pessoa.setUltimoNome("Último nome da pessoa " + i);
		pessoa.setEndereco("Algum endereço no Brasil " + i);
		pessoa.setGenero("Masculino");
		
		return pessoa;
	}
	
}
