package com.joseleonardo.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AutenticacaoControllerXmlTest extends AbstractIntegrationTest {

	private static TokenDTO tokenDTO;
	
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		CredenciaisDaContaDTO usuario = new CredenciaisDaContaDTO("leandro", "admin123");
		
		tokenDTO = given()
				.basePath("/autenticacao/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(usuario)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
					    		.as(TokenDTO.class);
		
		assertNotNull(tokenDTO.getAccessToken());
		assertNotNull(tokenDTO.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		TokenDTO newTokenDTO = given()
				.basePath("/autenticacao/refresh")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("nomeDeUsuario", tokenDTO.getNomeDeUsuario())
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
					.when()
				.put("{nomeDeUsuario}")
					.then()
						.statusCode(200)
							.extract()
							.body()
					    		.as(TokenDTO.class);
		
		assertNotNull(newTokenDTO.getAccessToken());
		assertNotNull(tokenDTO.getRefreshToken());
	}
	
}
