package com.joseleonardo.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.PessoaDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;
	
	private static PessoaDTO pessoa;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		pessoa = new PessoaDTO();
	}
	
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		CredenciaisDaContaDTO usuario = new CredenciaisDaContaDTO("leandro", "admin123");
		
		String accessToken = given()
				.basePath("/autenticacao/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(usuario)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDTO.class).getAccessToken();
		
		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/pessoas/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testSalvar() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(pessoa)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		PessoaDTO pessoaPersistida = objectMapper.readValue(content, PessoaDTO.class);
		pessoa = pessoaPersistida;
		
		assertNotNull(pessoaPersistida);
		
		assertNotNull(pessoaPersistida.getId());
		assertNotNull(pessoaPersistida.getPrimeiroNome());
		assertNotNull(pessoaPersistida.getUltimoNome());
		assertNotNull(pessoaPersistida.getEndereco());
		assertNotNull(pessoaPersistida.getGenero());
		
		assertTrue(pessoaPersistida.getId() > 0);
		
		assertEquals("Lúcia", pessoaPersistida.getPrimeiroNome());
		assertEquals("Márcia", pessoaPersistida.getUltimoNome());
		assertEquals("Santa Catarina", pessoaPersistida.getEndereco());
		assertEquals("Feminino", pessoaPersistida.getGenero());
	}
	
	@Test
	@Order(2)
	public void testAtualizar() throws JsonMappingException, JsonProcessingException {
		pessoa.setUltimoNome("Nogueira");
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(pessoa)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		PessoaDTO pessoaPersistida = objectMapper.readValue(content, PessoaDTO.class);
		pessoa = pessoaPersistida;
		
		assertNotNull(pessoaPersistida);
		
		assertNotNull(pessoaPersistida.getId());
		assertNotNull(pessoaPersistida.getPrimeiroNome());
		assertNotNull(pessoaPersistida.getUltimoNome());
		assertNotNull(pessoaPersistida.getEndereco());
		assertNotNull(pessoaPersistida.getGenero());
		
		assertEquals(pessoa.getId(), pessoaPersistida.getId());
		assertEquals("Lúcia", pessoaPersistida.getPrimeiroNome());
		assertEquals("Nogueira", pessoaPersistida.getUltimoNome());
		assertEquals("Santa Catarina", pessoaPersistida.getEndereco());
		assertEquals("Feminino", pessoaPersistida.getGenero());
	}
	
	@Test
	@Order(3)
	public void testBuscarPorId() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JOSE_LEONARDO)
				.pathParam("id", pessoa.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		PessoaDTO pessoaPersistida = objectMapper.readValue(content, PessoaDTO.class);
		pessoa = pessoaPersistida;
		
		assertNotNull(pessoaPersistida);
		
		assertNotNull(pessoaPersistida.getId());
		assertNotNull(pessoaPersistida.getPrimeiroNome());
		assertNotNull(pessoaPersistida.getUltimoNome());
		assertNotNull(pessoaPersistida.getEndereco());
		assertNotNull(pessoaPersistida.getGenero());
		
		assertTrue(pessoaPersistida.getId() > 0);
		
		assertEquals("Lúcia", pessoaPersistida.getPrimeiroNome());
		assertEquals("Nogueira", pessoaPersistida.getUltimoNome());
		assertEquals("Santa Catarina", pessoaPersistida.getEndereco());
		assertEquals("Feminino", pessoaPersistida.getGenero());
	}
	
	@Test
	@Order(4)
	public void testDeletar() throws JsonMappingException, JsonProcessingException {
		given().spec(requestSpecification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", pessoa.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5)
	public void testListarTodas() throws JsonMappingException, JsonProcessingException {
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
					 // .as(new TypeRef<List<PessoaDTO>>() {});
		
		List<PessoaDTO> pessoas = objectMapper.readValue(content, 
				new TypeReference<List<PessoaDTO>>() {});
		
		PessoaDTO pessoaUm = pessoas.get(0);
		
		assertNotNull(pessoaUm.getId());
		assertNotNull(pessoaUm.getPrimeiroNome());
		assertNotNull(pessoaUm.getUltimoNome());
		assertNotNull(pessoaUm.getEndereco());
		assertNotNull(pessoaUm.getGenero());
		
		assertEquals(1, pessoaUm.getId());
		
		assertEquals("Maria", pessoaUm.getPrimeiroNome());
		assertEquals("Helena", pessoaUm.getUltimoNome());
		assertEquals("São Paulo", pessoaUm.getEndereco());
		assertEquals("Feminino", pessoaUm.getGenero());
		
		PessoaDTO pessoaSeis = pessoas.get(5);
		
		assertNotNull(pessoaSeis.getId());
		assertNotNull(pessoaSeis.getPrimeiroNome());
		assertNotNull(pessoaSeis.getUltimoNome());
		assertNotNull(pessoaSeis.getEndereco());
		assertNotNull(pessoaSeis.getGenero());
		
		assertEquals(6, pessoaSeis.getId());
		
		assertEquals("Theo", pessoaSeis.getPrimeiroNome());
		assertEquals("Benício", pessoaSeis.getUltimoNome());
		assertEquals("Santa Catarina", pessoaSeis.getEndereco());
		assertEquals("Masculino", pessoaSeis.getGenero());
	}
	
	@Test
	@Order(6)
	public void testListarTodasSemToken() throws JsonMappingException, JsonProcessingException {
		RequestSpecification requestSpecificationSemToken = new RequestSpecBuilder()
				.setBasePath("/api/pessoas/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given().spec(requestSpecificationSemToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
					.get()
				.then()
					.statusCode(403)
				.extract()
					.body()
						.asString();
	}
	
	private void mockPessoa() {
		pessoa.setPrimeiroNome("Lúcia");
		pessoa.setUltimoNome("Márcia");
		pessoa.setEndereco("Santa Catarina");
		pessoa.setGenero("Feminino");
	}

}
