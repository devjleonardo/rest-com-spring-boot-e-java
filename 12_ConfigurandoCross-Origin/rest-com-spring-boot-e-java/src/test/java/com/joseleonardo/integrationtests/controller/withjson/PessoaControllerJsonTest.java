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
import com.joseleonardo.integrationtests.dto.PessoaDTO;
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
	@Order(1)
	public void testSalvar() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "https://joseleonardo.com")
				.setBasePath("/api/pessoas/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
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
		
		PessoaDTO pessoaCriada = objectMapper.readValue(content, PessoaDTO.class);
		pessoa = pessoaCriada;
		
		assertNotNull(pessoaCriada);
		
		assertNotNull(pessoaCriada.getId());
		assertNotNull(pessoaCriada.getPrimeiroNome());
		assertNotNull(pessoaCriada.getUltimoNome());
		assertNotNull(pessoaCriada.getEndereco());
		assertNotNull(pessoaCriada.getGenero());
		
		assertTrue(pessoaCriada.getId() > 0);
		
		assertEquals("Nicolas", pessoaCriada.getPrimeiroNome());
		assertEquals("Arthur", pessoaCriada.getUltimoNome());
		assertEquals("Rio Grande do Norte", pessoaCriada.getEndereco());
		assertEquals("Masculino", pessoaCriada.getGenero());
	}

	private void mockPessoa() {
		pessoa.setPrimeiroNome("Nicolas");
		pessoa.setUltimoNome("Arthur");
		pessoa.setEndereco("Rio Grande do Norte");
		pessoa.setGenero("Masculino");
	}

}
