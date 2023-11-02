package com.joseleonardo.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseleonardo.controllers.PessoaController;
import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.exceptions.ResourceNotFoundException;
import com.joseleonardo.mapper.DozerMapper;
import com.joseleonardo.model.Pessoa;
import com.joseleonardo.repositories.PessoaRepository;

@Service
public class PessoaService {

	private Logger logger = Logger.getLogger(PessoaService.class.getName());
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public List<PessoaDTO> listarTodas() {
		logger.info("Encontrar todas pessoas!");
		
		return DozerMapper.parseListObjects(pessoaRepository.findAll(), PessoaDTO.class);
	}
	
	public PessoaDTO buscarPorId(Long id) {
		logger.info("Encontrar uma pessoa!");
		
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		PessoaDTO pessoaDTO = DozerMapper.parseObject(pessoa, PessoaDTO.class);
		
		pessoaDTO.add(linkTo(methodOn(PessoaController.class).buscarPorId(id)).withSelfRel());
		
		return pessoaDTO;
	}
	
	public PessoaDTO salvar(PessoaDTO pessoaDTO) {
		logger.info("Criando uma pessoa!");
		
		Pessoa pessoa = DozerMapper.parseObject(pessoaDTO, Pessoa.class);
		
		return DozerMapper.parseObject(pessoaRepository.save(pessoa), PessoaDTO.class);
	}
	
	public PessoaDTO atualizar(PessoaDTO pessoaAtualizada) {
		logger.info("Atualizando uma pessoa!");
		
		Pessoa pessoaAtual = pessoaRepository.findById(pessoaAtualizada.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
		
		pessoaAtual.setPrimeiroNome(pessoaAtualizada.getPrimeiroNome());
		pessoaAtual.setUltimoNome(pessoaAtualizada.getUltimoNome());
		pessoaAtual.setEndereco(pessoaAtualizada.getEndereco());
		pessoaAtual.setGenero(pessoaAtualizada.getGenero());
		
		return DozerMapper.parseObject(pessoaRepository.save(pessoaAtual), PessoaDTO.class);
	}
	
	public void deletar(Long id) {
		logger.info("Deletando uma pessoa!");
		
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		pessoaRepository.delete(pessoa);
	}
	
}
