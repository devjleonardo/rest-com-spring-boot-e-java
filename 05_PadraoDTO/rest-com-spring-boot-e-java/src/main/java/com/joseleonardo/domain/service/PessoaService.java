package com.joseleonardo.domain.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseleonardo.domain.entity.PessoaDTO;
import com.joseleonardo.domain.repository.PessoaRepository;
import com.joseleonardo.exception.ResourceNotFoundException;

@Service
public class PessoaService {

	private Logger logger = Logger.getLogger(PessoaService.class.getName());
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public List<PessoaDTO> listarTodas() {
		logger.info("Encontrar todas pessoas!");
		
		return pessoaRepository.findAll();
	}
	
	public PessoaDTO buscarPorId(Long id) {
		logger.info("Encontrar uma pessoa!");
		
		return pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	}
	
	public PessoaDTO salvar(PessoaDTO pessoa) {
		logger.info("Criando uma pessoa!");
		
		return pessoaRepository.save(pessoa);
	}
	
	public PessoaDTO atualizar(PessoaDTO pessoaAtualizada) {
		logger.info("Atualizando uma pessoa!");
		
		PessoaDTO pessoaAtual = pessoaRepository.findById(pessoaAtualizada.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
		
		pessoaAtual.setPrimeiroNome(pessoaAtualizada.getPrimeiroNome());
		pessoaAtual.setUltimoNome(pessoaAtualizada.getUltimoNome());
		pessoaAtual.setEndereco(pessoaAtualizada.getEndereco());
		pessoaAtual.setGenero(pessoaAtualizada.getGenero());
		
		return pessoaRepository.save(pessoaAtual);
	}
	
	public void deletar(Long id) {
		logger.info("Deletando uma pessoa!");
		
		PessoaDTO pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		pessoaRepository.delete(pessoa);
	}
	
}
