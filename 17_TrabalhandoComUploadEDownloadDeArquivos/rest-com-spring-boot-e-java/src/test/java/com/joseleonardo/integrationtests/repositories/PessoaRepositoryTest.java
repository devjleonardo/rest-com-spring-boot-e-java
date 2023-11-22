package com.joseleonardo.integrationtests.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;
import com.joseleonardo.model.Pessoa;
import com.joseleonardo.repositories.PessoaRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private static Pessoa pessoa;
	
	@BeforeAll
	public static void setup() {
		pessoa = new Pessoa();
	}
	
	@Test
	@Order(1)
	public void testBuscarPorNome() throws JsonMappingException, JsonProcessingException {
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "primeiroNome"));
		pessoa = pessoaRepository.buscarPessoasPorNome("heo", pageable).getContent().get(0);
		
		assertNotNull(pessoa.getId());
		assertNotNull(pessoa.getPrimeiroNome());
		assertNotNull(pessoa.getUltimoNome());
		assertNotNull(pessoa.getEndereco());
		assertNotNull(pessoa.getGenero());
		
		assertTrue(pessoa.getHabilitada());
		
		assertEquals(6, pessoa.getId());
		assertEquals("Theo", pessoa.getPrimeiroNome());
		assertEquals("Benício", pessoa.getUltimoNome());
		assertEquals("Santa Catarina", pessoa.getEndereco());
		assertEquals("Masculino", pessoa.getGenero());
	}
	
	@Test
	@Order(2)
	public void testDesabilitarPessoa() throws JsonMappingException, JsonProcessingException {
		pessoaRepository.desabilitarPessoa(pessoa.getId());
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "primeiroNome"));
		pessoa = pessoaRepository.buscarPessoasPorNome("heo", pageable).getContent().get(0);
		
		assertNotNull(pessoa.getId());
		assertNotNull(pessoa.getPrimeiroNome());
		assertNotNull(pessoa.getUltimoNome());
		assertNotNull(pessoa.getEndereco());
		assertNotNull(pessoa.getGenero());
		
		assertFalse(pessoa.getHabilitada());
		
		assertEquals(6, pessoa.getId());
		assertEquals("Theo", pessoa.getPrimeiroNome());
		assertEquals("Benício", pessoa.getUltimoNome());
		assertEquals("Santa Catarina", pessoa.getEndereco());
		assertEquals("Masculino", pessoa.getGenero());
	}
	
}
