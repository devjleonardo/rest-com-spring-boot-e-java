package com.joseleonardo.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.PessoaDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.dto.pagedmodels.PagedModelPessoa;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification requestSpecification;
	private static XmlMapper xmlMapper;
	
	private static PessoaDTO pessoa;
	
	@BeforeAll
	public static void setup() {
		xmlMapper = new XmlMapper();
		xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		pessoa = new PessoaDTO();
	}
	
	
	@Test
	@Order(1)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		CredenciaisDaContaDTO usuario = new CredenciaisDaContaDTO("leandro", "admin123");
		
		String accessToken = given()
				.basePath("/autenticacao/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
					.accept(TestConfigs.CONTENT_TYPE_XML)
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
	@Order(2)
	public void testSalvar() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(pessoa)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		pessoa = xmlMapper.readValue(content, PessoaDTO.class);
		
		assertNotNull(pessoa);
		assertNotNull(pessoa.getId());
		assertNotNull(pessoa.getPrimeiroNome());
		assertNotNull(pessoa.getUltimoNome());
		assertNotNull(pessoa.getEndereco());
		assertNotNull(pessoa.getGenero());
		
		assertTrue(pessoa.getId() > 0);
		assertTrue(pessoa.getHabilitada());
		
		assertEquals("Lúcia", pessoa.getPrimeiroNome());
		assertEquals("Márcia", pessoa.getUltimoNome());
		assertEquals("Santa Catarina", pessoa.getEndereco());
		assertEquals("Feminino", pessoa.getGenero());
	}
	
	@Test
	@Order(3)
	public void testAtualizar() throws JsonMappingException, JsonProcessingException {
		pessoa.setUltimoNome("Nogueira");
		
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(pessoa)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		PessoaDTO pessoaAtualizada = xmlMapper.readValue(content, PessoaDTO.class);

		assertNotNull(pessoaAtualizada);
		assertNotNull(pessoaAtualizada.getId());
		assertNotNull(pessoaAtualizada.getPrimeiroNome());
		assertNotNull(pessoaAtualizada.getUltimoNome());
		assertNotNull(pessoaAtualizada.getEndereco());
		assertNotNull(pessoaAtualizada.getGenero());
		
		assertTrue(pessoa.getHabilitada());
		
		assertEquals(pessoa.getId(), pessoaAtualizada.getId());
		assertEquals("Lúcia", pessoaAtualizada.getPrimeiroNome());
		assertEquals("Nogueira", pessoaAtualizada.getUltimoNome());
		assertEquals("Santa Catarina", pessoaAtualizada.getEndereco());
		assertEquals("Feminino", pessoaAtualizada.getGenero());
	}
	
	@Test
	@Order(4)
	public void testDesabitarPessoaPorId() throws JsonMappingException, JsonProcessingException {
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", pessoa.getId())
				.when()
					.patch("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		PessoaDTO pessoaBuscada = xmlMapper.readValue(content, PessoaDTO.class);
		
		assertNotNull(pessoaBuscada);
		assertNotNull(pessoaBuscada.getId());
		assertNotNull(pessoaBuscada.getPrimeiroNome());
		assertNotNull(pessoaBuscada.getUltimoNome());
		assertNotNull(pessoaBuscada.getEndereco());
		assertNotNull(pessoaBuscada.getGenero());
		
		assertFalse(pessoaBuscada.getHabilitada());
		
		assertEquals(pessoa.getId(), pessoaBuscada.getId());
		assertEquals("Lúcia", pessoaBuscada.getPrimeiroNome());
		assertEquals("Nogueira", pessoaBuscada.getUltimoNome());
		assertEquals("Santa Catarina", pessoaBuscada.getEndereco());
		assertEquals("Feminino", pessoaBuscada.getGenero());
	}
	
	@Test
	@Order(5)
	public void testBuscarPorId() throws JsonMappingException, JsonProcessingException {
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JOSE_LEONARDO)
				.pathParam("id", pessoa.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
					    .asString();
		
		PessoaDTO pessoaBuscada = xmlMapper.readValue(content, PessoaDTO.class);
		
		assertNotNull(pessoaBuscada);
		assertNotNull(pessoaBuscada.getId());
		assertNotNull(pessoaBuscada.getPrimeiroNome());
		assertNotNull(pessoaBuscada.getUltimoNome());
		assertNotNull(pessoaBuscada.getEndereco());
		assertNotNull(pessoaBuscada.getGenero());
		
		assertFalse(pessoaBuscada.getHabilitada());
		
		assertEquals(pessoa.getId(), pessoaBuscada.getId());
		assertEquals("Lúcia", pessoaBuscada.getPrimeiroNome());
		assertEquals("Nogueira", pessoaBuscada.getUltimoNome());
		assertEquals("Santa Catarina", pessoaBuscada.getEndereco());
		assertEquals("Feminino", pessoaBuscada.getGenero());
	}
	
	@Test
	@Order(6)
	public void testDeletar() throws JsonMappingException, JsonProcessingException {
		given().spec(requestSpecification)
		        .contentType(TestConfigs.CONTENT_TYPE_XML)
			    .accept(TestConfigs.CONTENT_TYPE_XML)
			    .pathParam("id", pessoa.getId())
			    .when()
				    .delete("{id}")
			    .then()
				    .statusCode(204);
	}
	
	@Test
	@Order(7)
	public void testListarTodas() throws JsonMappingException, JsonProcessingException {
		String content = given().spec(requestSpecification)
		        .contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
					 // .as(new TypeRef<List<PessoaDTO>>() {});
		
		PagedModelPessoa pagedModelPessoa = xmlMapper.readValue(content, PagedModelPessoa.class);
		List<PessoaDTO> pessoas = pagedModelPessoa.getContent();
		
		PessoaDTO pessoaUm = pessoas.get(0);
		
		assertNotNull(pessoaUm.getId());
		assertNotNull(pessoaUm.getPrimeiroNome());
		assertNotNull(pessoaUm.getUltimoNome());
		assertNotNull(pessoaUm.getEndereco());
		assertNotNull(pessoaUm.getGenero());
		
		assertTrue(pessoaUm.getHabilitada());
		
		assertEquals(151, pessoaUm.getId());
		assertEquals("Allie", pessoaUm.getPrimeiroNome());
		assertEquals("Cavey", pessoaUm.getUltimoNome());
		assertEquals("653 Maple Wood Plaza", pessoaUm.getEndereco());
		assertEquals("Female", pessoaUm.getGenero());
		
		PessoaDTO pessoaSeis = pessoas.get(5);
		
		assertNotNull(pessoaSeis.getId());
		assertNotNull(pessoaSeis.getPrimeiroNome());
		assertNotNull(pessoaSeis.getUltimoNome());
		assertNotNull(pessoaSeis.getEndereco());
		assertNotNull(pessoaSeis.getGenero());
		
		assertFalse(pessoaSeis.getHabilitada());
		
		assertEquals(371, pessoaSeis.getId());
		assertEquals("Alwin", pessoaSeis.getPrimeiroNome());
		assertEquals("Barrasse", pessoaSeis.getUltimoNome());
		assertEquals("95512 Artisan Alley", pessoaSeis.getEndereco());
		assertEquals("Male", pessoaSeis.getGenero());
	}
	
	@Test
	@Order(8)
	public void testBuscarPorNome() throws JsonMappingException, JsonProcessingException {
		String content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("primeiroNome", "heo")
				.queryParams("page", 0, "size", 6, "direction", "asc")
				.when()
					.get("buscar-pessoas-por-nome/{primeiroNome}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		PagedModelPessoa wrapper = xmlMapper.readValue(content, PagedModelPessoa.class);
		List<PessoaDTO> pessoas = wrapper.getContent();
	
		PessoaDTO pessoaUm = pessoas.get(0);
		
		assertNotNull(pessoaUm.getId());
		assertNotNull(pessoaUm.getPrimeiroNome());
		assertNotNull(pessoaUm.getUltimoNome());
		assertNotNull(pessoaUm.getEndereco());
		assertNotNull(pessoaUm.getGenero());
		
		assertTrue(pessoaUm.getHabilitada());
		
		assertEquals(6, pessoaUm.getId());
		assertEquals("Theo", pessoaUm.getPrimeiroNome());
		assertEquals("Benício", pessoaUm.getUltimoNome());
		assertEquals("Santa Catarina", pessoaUm.getEndereco());
		assertEquals("Masculino", pessoaUm.getGenero());
	}
	
	@Test
	@Order(9)
	public void testListarTodasSemToken() throws JsonMappingException, JsonProcessingException {
		RequestSpecification requestSpecificationSemToken = new RequestSpecBuilder()
		        .setBasePath("/api/pessoas/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given().spec(requestSpecificationSemToken)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
		pessoa.setHabilitada(true);
	}

}
