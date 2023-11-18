package com.joseleonardo.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseleonardo.controllers.LivroController;
import com.joseleonardo.data.dto.v1.LivroDTO;
import com.joseleonardo.exceptions.RequiredObjectIsNullException;
import com.joseleonardo.exceptions.ResourceNotFoundException;
import com.joseleonardo.mapper.DozerMapper;
import com.joseleonardo.model.Livro;
import com.joseleonardo.repositories.LivroRepository;

@Service
public class LivroService {

	private Logger logger = Logger.getLogger(LivroService.class.getName());
	
	@Autowired
	private LivroRepository livroRepository;
	
	public List<LivroDTO> listarTodos() {
		logger.info("Encontrar todos livros!");
		
		List<LivroDTO> livros = DozerMapper.parseListObjects(livroRepository.findAll(), LivroDTO.class);
		
		livros.stream()
			.forEach(livro -> livro.add(linkTo(methodOn(LivroController.class)
						.buscarPorId(livro.getId())).withSelfRel()));
		
		return livros;
	}
	
	public LivroDTO buscarPorId(Long id) {
		logger.info("Encontrar um livro!");
		
		Livro entity = livroRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		LivroDTO dto = DozerMapper.parseObject(entity, LivroDTO.class);
		
		dto.add(linkTo(methodOn(LivroController.class).buscarPorId(id)).withSelfRel());
		
		return dto;
	}
	
	public LivroDTO salvar(LivroDTO livro) {
		if (livro == null) {
			throw new RequiredObjectIsNullException();
		}
		
		logger.info("Criando um livro!");
		
		Livro entity = DozerMapper.parseObject(livro, Livro.class);
		
		LivroDTO dto = DozerMapper.parseObject(livroRepository.save(entity), LivroDTO.class);
		
		dto.add(linkTo(methodOn(LivroController.class).buscarPorId(dto.getId())).withSelfRel());
		
		return dto;
	}
	
	public LivroDTO atualizar(LivroDTO pessoaAtualizada) {
		if (pessoaAtualizada == null) {
			throw new RequiredObjectIsNullException();
		}
		
		logger.info("Atualizando um livro!");
		
		Livro entity = livroRepository.findById(pessoaAtualizada.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
		
		entity.setAutor(pessoaAtualizada.getAutor());
		entity.setDataLancamento(pessoaAtualizada.getDataLancamento());
		entity.setPreco(pessoaAtualizada.getPreco());
		entity.setTitulo(pessoaAtualizada.getTitulo());
		
		LivroDTO dto = DozerMapper.parseObject(livroRepository.save(entity), LivroDTO.class);
		
		dto.add(linkTo(methodOn(LivroController.class).buscarPorId(dto.getId())).withSelfRel());
		
		return dto;
	}
	
	public void deletar(Long id) {
		logger.info("Deletando um livro!");
		
		Livro entity = livroRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Não encontramos nenhum registro para este ID!"));
	
		livroRepository.delete(entity);
	}
	
}
