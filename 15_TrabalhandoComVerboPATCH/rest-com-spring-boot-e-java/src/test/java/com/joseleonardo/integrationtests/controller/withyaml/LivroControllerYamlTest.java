package com.joseleonardo.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
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
import com.joseleonardo.integrationtests.dto.LivroDTO;
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
public class LivroControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;

    private static YMLMapper ymlMapper;

    private static LivroDTO livro;

    @BeforeAll
    public static void setup() {
        ymlMapper = new YMLMapper();
        livro = new LivroDTO();
    }
    
    @Test
    @Order(1)
    public void authorization() {
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
                .setBasePath("/api/livros/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }
      
    @Test
    @Order(2)
    public void testSalvar() throws JsonMappingException, JsonProcessingException {
    	 mockLivro();
    	 
    	 livro = given().spec(requestSpecification)
                 .config(
                     RestAssuredConfig
                         .config()
                             .encoderConfig(EncoderConfig.encoderConfig()
                                 .encodeContentTypeAs(
                                     TestConfigs.CONTENT_TYPE_YML, 
                                     ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
				 .accept(TestConfigs.CONTENT_TYPE_YML)
                 .body(livro, ymlMapper)
                 .when()
                 	.post()
                 .then()
                 	.statusCode(200)
                 		.extract()
                 			.body()
                 				.as(LivroDTO.class, ymlMapper);
        
        assertNotNull(livro.getId());
        assertNotNull(livro.getTitulo());
        assertNotNull(livro.getAutor());
        assertNotNull(livro.getPreco());
        
        assertTrue(livro.getId() > 0);
        
        assertEquals("Docker Deep Dive", livro.getTitulo());
        assertEquals("Nigel Poulton", livro.getAutor());
        assertEquals(55.99, livro.getPreco());
    }
      
    @Test
    @Order(3)
    public void testAtualizar() throws JsonMappingException, JsonProcessingException {
        livro.setTitulo("Docker Deep Dive - Updated");

        LivroDTO livroAtualizado = given().spec(requestSpecification)
        		.config(
        		    RestAssuredConfig
        		    	.config()
                        	.encoderConfig(EncoderConfig.encoderConfig()
                        			.encodeContentTypeAs(
                        				TestConfigs.CONTENT_TYPE_YML, 
                        				ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
			    .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(livro, ymlMapper)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .as(LivroDTO.class, ymlMapper);
        
        assertNotNull(livroAtualizado.getId());
        assertNotNull(livroAtualizado.getTitulo());
        assertNotNull(livroAtualizado.getAutor());
        assertNotNull(livroAtualizado.getPreco());
        
        assertEquals(livro.getId(), livroAtualizado.getId());
        assertEquals("Docker Deep Dive - Updated", livroAtualizado.getTitulo());
        assertEquals("Nigel Poulton", livroAtualizado.getAutor());
        assertEquals(55.99, livroAtualizado.getPreco());
    }

    @Test
    @Order(4)
    public void testBuscarPorId() throws JsonMappingException, JsonProcessingException {
        LivroDTO livroBuscado = given().spec(requestSpecification)
                .config(
                    RestAssuredConfig
                        .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(
                                    TestConfigs.CONTENT_TYPE_YML, 
                                    ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", livro.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .as(LivroDTO.class, ymlMapper);
        
        assertNotNull(livroBuscado);
        assertNotNull(livroBuscado.getId());
        assertNotNull(livroBuscado.getTitulo());
        assertNotNull(livroBuscado.getAutor());
        assertNotNull(livroBuscado.getPreco());
        
        assertEquals(livro.getId(), livroBuscado.getId());
        assertEquals("Docker Deep Dive - Updated", livroBuscado.getTitulo());
        assertEquals("Nigel Poulton", livroBuscado.getAutor());
        assertEquals(55.99, livroBuscado.getPreco());
    }
    
    @Test
    @Order(5)
    public void testDeletar() {
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
                .pathParam("id", livro.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
    
    @Test
    @Order(6)
    public void testListarTodos() throws JsonMappingException, JsonProcessingException {
    	LivroDTO[] content = given().spec(requestSpecification)
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
                        .as(LivroDTO[].class, ymlMapper); 

        List<LivroDTO> livros = Arrays.asList(content);

        LivroDTO livroUm = livros.get(0);
        
        assertNotNull(livroUm);
        assertNotNull(livroUm.getId());
        assertNotNull(livroUm.getTitulo());
        assertNotNull(livroUm.getAutor());
        assertNotNull(livroUm.getPreco());
        
        assertEquals(1, livroUm.getId());
        assertEquals("Working effectively with legacy code", livroUm.getTitulo());
        assertEquals("Michael C. Feathers", livroUm.getAutor());
        assertEquals(49.00, livroUm.getPreco());
        
        LivroDTO livroCinco = livros.get(4);
        
        assertNotNull(livroCinco);
        assertNotNull(livroCinco.getId());
        assertNotNull(livroCinco.getTitulo());
        assertNotNull(livroCinco.getAutor());
        assertNotNull(livroCinco.getPreco());
        
        assertEquals(5, livroCinco.getId());
        assertEquals("Code complete", livroCinco.getTitulo());
        assertEquals("Steve McConnell", livroCinco.getAutor());
        assertEquals(58.0, livroCinco.getPreco());
    }
    
	@Test
	@Order(7)
	public void testListarTodasSemToken() throws JsonMappingException, JsonProcessingException {
		RequestSpecification requestSpecificationSemToken = new RequestSpecBuilder()
		        .setBasePath("/api/pessoas/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given().spec(requestSpecificationSemToken)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
					.get()
				.then()
					.statusCode(403)
				.extract()
					.body()
						.asString();
	}
     
    private void mockLivro() {
        livro.setTitulo("Docker Deep Dive");
        livro.setAutor("Nigel Poulton");
        livro.setPreco(Double.valueOf(55.99));
        livro.setDataLancamento(new Date());
    }
}