package com.joseleonardo.domain.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseleonardo.domain.entity.Pessoa;
import com.joseleonardo.domain.repository.PessoaRepository;
import com.joseleonardo.exception.ResourceNotFoundException;

@Service
public class PessoaService {

	private Logger logger = Logger.getLogger(PessoaService.class.getName());
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public List<Pessoa> listarTodas() {
		logger.info("Encontrar todas pessoas!");
		
		return pessoaRepository.findAll();
	}
	
	public Pessoa buscarPorId(Long id) {
		logger.info("Encontrar uma pessoa!");
		
		return pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro econtrado para este ID!"));
	}
	
	public Pessoa salvar(Pessoa pessoa) {
		logger.info("Criando uma pessoa!");
		
		return pessoaRepository.save(pessoa);
	}
	
	public Pessoa atualizar(Pessoa pessoaAtualizada) {
		logger.info("Atualizando uma pessoa!");
		
		Pessoa pessoaAtual = pessoaRepository.findById(pessoaAtualizada.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro econtrado para este ID!"));
		
		pessoaAtual.setPrimeiroNome(pessoaAtualizada.getPrimeiroNome());
		pessoaAtual.setUltimoNome(pessoaAtualizada.getUltimoNome());
		pessoaAtual.setEndereco(pessoaAtualizada.getEndereco());
		pessoaAtual.setGenero(pessoaAtualizada.getGenero());
		
		return pessoaAtualizada;
	}
	
	public void deletar(Long id) {
		logger.info("Deletando uma pessoa!");
		
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro econtrado para este ID!"));
	
		pessoaRepository.delete(pessoa);
	}
	
}
