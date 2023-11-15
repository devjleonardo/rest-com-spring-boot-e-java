package com.joseleonardo.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.PessoaDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification requestSpecification;
	private static YMLMapper ymlMapper;
	
	private static PessoaDTO pessoa;
	
	@BeforeAll
	public static void setup() {
		ymlMapper = new YMLMapper();
		pessoa = new PessoaDTO();
	}
	
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		CredenciaisDaContaDTO usuario = new CredenciaisDaContaDTO("leandro", "admin123");
		
		String accessToken = given()
				.config(
					RestAssuredConfig
						.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML, 
									ContentType.TEXT)))
				.basePath("/autenticacao/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(usuario, ymlMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDTO.class, ymlMapper).getAccessToken();
		
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
		
		PessoaDTO pessoaPersistida = given().spec(requestSpecification)
				.config(
					RestAssuredConfig
						.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(pessoa, ymlMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(PessoaDTO.class, ymlMapper);
		
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
		
		PessoaDTO pessoaPersistida = given().spec(requestSpecification)
				.config(
					RestAssuredConfig
						.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML, 
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(pessoa, ymlMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(PessoaDTO.class, ymlMapper);
		
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
		
		PessoaDTO pessoaPersistida = given().spec(requestSpecification)
				.config(
					RestAssuredConfig
						.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML, 
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JOSE_LEONARDO)
				.pathParam("id", pessoa.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(PessoaDTO.class, ymlMapper);
		
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
			.config(
				RestAssuredConfig
					.config()
						.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(
								TestConfigs.CONTENT_TYPE_YML, 
								ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
			.pathParam("id", pessoa.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5)
	public void testListarTodas() throws JsonMappingException, JsonProcessingException {
		PessoaDTO[] content = given().spec(requestSpecification)
				.config(
					RestAssuredConfig
						.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML, 
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(PessoaDTO[].class, ymlMapper);
					 // .as(new TypeRef<List<PessoaDTO>>() {});
		
		List<PessoaDTO> pessoas = Arrays.asList(content);
		
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
			.config(
				RestAssuredConfig
					.config()
						.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(
								TestConfigs.CONTENT_TYPE_YML, 
								ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
					.get()
				.then()
					.statusCode(403);
	}
	
	private void mockPessoa() {
		pessoa.setPrimeiroNome("Lúcia");
		pessoa.setUltimoNome("Márcia");
		pessoa.setEndereco("Santa Catarina");
		pessoa.setGenero("Feminino");
	}

}
