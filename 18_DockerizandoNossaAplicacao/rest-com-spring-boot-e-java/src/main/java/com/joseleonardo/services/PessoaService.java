package com.joseleonardo.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.joseleonardo.controllers.PessoaController;
import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.exceptions.RequiredObjectIsNullException;
import com.joseleonardo.exceptions.ResourceNotFoundException;
import com.joseleonardo.mapper.DozerMapper;
import com.joseleonardo.model.Pessoa;
import com.joseleonardo.repositories.PessoaRepository;

import jakarta.transaction.Transactional;

@Service
public class PessoaService {

	private Logger logger = Logger.getLogger(PessoaService.class.getName());
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PagedResourcesAssembler<PessoaDTO> assembler;
	
	public PagedModel<EntityModel<PessoaDTO>> listarTodas(Pageable pageable) {
		logger.info("Encontrar todas pessoas!");
		
		Page<Pessoa> pessoasPage = pessoaRepository.findAll(pageable);
		
		Page<PessoaDTO> pessoasDTOPage = pessoasPage.map(
				pessoa -> DozerMapper.parseObject(pessoa, PessoaDTO.class));
		
		pessoasDTOPage.map(
				pessoa -> pessoa.add(
				    linkTo(methodOn(PessoaController.class)
					    .buscarPorId(pessoa.getId())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PessoaController.class)
				    .listar(pageable.getPageNumber(), 
				    		pageable.getPageSize(), 
				    		"asc")).withSelfRel();
		
		return assembler.toModel(pessoasDTOPage, link);
	}
	
	public PagedModel<EntityModel<PessoaDTO>> buscarPessoasPorNome(String primeiroNome, Pageable pageable) {
		logger.info("Encontrar todas pessoas!");
		
		Page<Pessoa> pessoasPage = pessoaRepository.buscarPessoasPorNome(primeiroNome, pageable);
		
		Page<PessoaDTO> pessoasDTOPage = pessoasPage.map(
				pessoa -> DozerMapper.parseObject(pessoa, PessoaDTO.class));
		
		pessoasDTOPage.map(
				pessoa -> pessoa.add(
				    linkTo(methodOn(PessoaController.class)
					    .buscarPorId(pessoa.getId())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PessoaController.class)
				    .listar(pageable.getPageNumber(), 
				    		pageable.getPageSize(), 
				    		"asc")).withSelfRel();
		
		return assembler.toModel(pessoasDTOPage, link);
	}
	
	public PessoaDTO buscarPorId(Long id) {
		logger.info("Encontrar uma pessoa!");
		
		Pessoa entity = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		PessoaDTO dto = DozerMapper.parseObject(entity, PessoaDTO.class);
		
		dto.add(linkTo(methodOn(PessoaController.class).buscarPorId(id)).withSelfRel());
		
		return dto;
	}
	
	public PessoaDTO salvar(PessoaDTO pessoa) {
		if (pessoa == null) {
			throw new RequiredObjectIsNullException();
		}
		
		logger.info("Criando uma pessoa!");
		
		Pessoa entity = DozerMapper.parseObject(pessoa, Pessoa.class);
		
		PessoaDTO dto = DozerMapper.parseObject(pessoaRepository.save(entity), PessoaDTO.class);
		
		dto.add(linkTo(methodOn(PessoaController.class).buscarPorId(dto.getId())).withSelfRel());
		
		return dto;
	}
	
	public PessoaDTO atualizar(PessoaDTO pessoaAtualizada) {
		if (pessoaAtualizada == null) {
			throw new RequiredObjectIsNullException();
		}
		
		logger.info("Atualizando uma pessoa!");
		
		Pessoa entity = pessoaRepository.findById(pessoaAtualizada.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
		
		entity.setPrimeiroNome(pessoaAtualizada.getPrimeiroNome());
		entity.setUltimoNome(pessoaAtualizada.getUltimoNome());
		entity.setEndereco(pessoaAtualizada.getEndereco());
		entity.setGenero(pessoaAtualizada.getGenero());
		
		PessoaDTO dto = DozerMapper.parseObject(pessoaRepository.save(entity), PessoaDTO.class);
		
		dto.add(linkTo(methodOn(PessoaController.class).buscarPorId(dto.getId())).withSelfRel());
		
		return dto;
	}
	
	@Transactional
	public PessoaDTO desabilitarPessoa(Long id) {
		logger.info("Desabilitando uma pessoa!");
		
		pessoaRepository.desabilitarPessoa(id);
		
		Pessoa entity = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		PessoaDTO dto = DozerMapper.parseObject(entity, PessoaDTO.class);
		
		dto.add(linkTo(methodOn(PessoaController.class).buscarPorId(id)).withSelfRel());
		
		return dto;
	}
	
	public void deletar(Long id) {
		logger.info("Deletando uma pessoa!");
		
		Pessoa entity = pessoaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		pessoaRepository.delete(entity);
	}
	
}
