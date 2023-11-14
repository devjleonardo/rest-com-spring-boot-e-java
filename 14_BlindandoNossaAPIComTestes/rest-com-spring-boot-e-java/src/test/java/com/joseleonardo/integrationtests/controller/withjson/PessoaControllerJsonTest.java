package com.joseleonardo.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
					    		.as(TokenDTO.class)
					    	.getAccessToken();
		
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
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JOSE_LEONARDO)
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
		
		assertEquals("Nicolas", pessoaPersistida.getPrimeiroNome());
		assertEquals("Arthur", pessoaPersistida.getUltimoNome());
		assertEquals("Rio Grande do Norte", pessoaPersistida.getEndereco());
		assertEquals("Masculino", pessoaPersistida.getGenero());
	}
	
	@Test
	@Order(2)
	public void testSalvarComOriginErrado() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JLEONARDO)
					.body(pessoa)
					.when()
					.post()
				.then()
					.statusCode(403)
				.extract()
					.body()
					    .asString();
		
		assertNotNull(content);
		
		assertEquals("Invalid CORS request", content);
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
		
		assertEquals("Nicolas", pessoaPersistida.getPrimeiroNome());
		assertEquals("Arthur", pessoaPersistida.getUltimoNome());
		assertEquals("Rio Grande do Norte", pessoaPersistida.getEndereco());
		assertEquals("Masculino", pessoaPersistida.getGenero());
	}
	
	@Test
	@Order(4)
	public void testBuscarPorIdComOriginErrado() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JLEONARDO)
					.pathParam("id", pessoa.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(403)
				.extract()
					.body()
					    .asString();
		
		assertNotNull(content);
		
		assertEquals("Invalid CORS request", content);
	}

	private void mockPessoa() {
		pessoa.setPrimeiroNome("Nicolas");
		pessoa.setUltimoNome("Arthur");
		pessoa.setEndereco("Rio Grande do Norte");
		pessoa.setGenero("Masculino");
	}

}
