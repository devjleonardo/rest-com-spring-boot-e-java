package com.joseleonardo.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.model.Pessoa;
import com.joseleonardo.repositories.PessoaRepository;
import com.joseleonardo.services.PessoaService;
import com.joseleonardo.unittests.mapper.mocks.MockPessoa;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

	private MockPessoa input;
	
	@InjectMocks
	private PessoaService pessoaService;
	
	@Mock
	private PessoaRepository pessoaRepository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPessoa();
		
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarTodas() {
		fail("Not yet implemented");
	}

	@Test
	void testBuscarPorId() {
		Pessoa entity = input.mockEntity(1);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(entity));
		
		PessoaDTO resultado = pessoaService.buscarPorId(1L);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getLinks());
		
		assertTrue(resultado.toString().contains("[</api/pessoas/v1/1>;rel=\"self\"]"));
		
		assertEquals("Primeiro nome teste1", resultado.getPrimeiroNome());
		assertEquals("Último nome teste1", resultado.getUltimoNome());
		assertEquals("Endereço teste1", resultado.getEndereco());
		assertEquals("Feminino", resultado.getGenero());
	}

	@Test
	void testSalvar() {
		Pessoa entity = input.mockEntity(1);
		Pessoa persistido = entity;
		
		when(pessoaRepository.save(entity)).thenReturn(persistido);
		
		PessoaDTO dto = input.mockVO(1);
		dto.setId(1L);
		
		PessoaDTO resultado = pessoaService.salvar(dto);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getLinks());
		
		assertTrue(resultado.toString().contains("[</api/pessoas/v1/1>;rel=\"self\"]"));
		
		assertEquals("Primeiro nome teste1", resultado.getPrimeiroNome());
		assertEquals("Último nome teste1", resultado.getUltimoNome());
		assertEquals("Endereço teste1", resultado.getEndereco());
		assertEquals("Feminino", resultado.getGenero());
	}

	@Test
	void testAtualizar() {
		Pessoa entity = input.mockEntity(1);
		Pessoa persistido = entity;
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(pessoaRepository.save(entity)).thenReturn(persistido);
		
		PessoaDTO dto = input.mockVO(1);
		dto.setId(1L);
		
		PessoaDTO resultado = pessoaService.atualizar(dto);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getLinks());
		
		assertTrue(resultado.toString().contains("[</api/pessoas/v1/1>;rel=\"self\"]"));
		
		assertEquals("Primeiro nome teste1", resultado.getPrimeiroNome());
		assertEquals("Último nome teste1", resultado.getUltimoNome());
		assertEquals("Endereço teste1", resultado.getEndereco());
		assertEquals("Feminino", resultado.getGenero());
	}

	@Test
	void testDeletar() {
		Pessoa entity = input.mockEntity(1);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(entity));
		
		pessoaService.deletar(1L);
	}

}
