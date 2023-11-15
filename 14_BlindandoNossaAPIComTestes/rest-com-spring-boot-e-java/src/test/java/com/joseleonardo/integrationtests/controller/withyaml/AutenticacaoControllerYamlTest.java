package com.joseleonardo.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
public class AutenticacaoControllerYamlTest extends AbstractIntegrationTest {

	private static YMLMapper ymlMapper;
	private static TokenDTO tokenDTO;
	
	@BeforeAll
	public static void setup() {
		ymlMapper = new YMLMapper();
	}
	
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		CredenciaisDaContaDTO usuario = new CredenciaisDaContaDTO("leandro", "admin123");
		
		RequestSpecification requestSpecification = new RequestSpecBuilder()
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		tokenDTO = given().spec(requestSpecification)
				.config(
					RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
										TestConfigs.CONTENT_TYPE_YML, 
										ContentType.TEXT)))
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.basePath("/autenticacao/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.body(usuario, ymlMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDTO.class, ymlMapper);
		
		assertNotNull(tokenDTO.getAccessToken());
		assertNotNull(tokenDTO.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		TokenDTO newTokenDTO = given()
				.config(
					RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
										TestConfigs.CONTENT_TYPE_YML, 
										ContentType.TEXT)))
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.basePath("/autenticacao/refresh")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("nomeDeUsuario", tokenDTO.getNomeDeUsuario())
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
				.when()
					.put("{nomeDeUsuario}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDTO.class, ymlMapper);
		
		assertNotNull(newTokenDTO.getAccessToken());
		assertNotNull(tokenDTO.getRefreshToken());
	}
	
}
