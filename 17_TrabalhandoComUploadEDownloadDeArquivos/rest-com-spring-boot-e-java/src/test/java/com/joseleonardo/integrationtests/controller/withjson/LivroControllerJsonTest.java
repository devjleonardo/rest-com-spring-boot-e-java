package com.joseleonardo.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.LivroDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.dto.wrappers.WrapperLivroDTO;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class LivroControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper objectMapper;

    private static LivroDTO livro;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        livro = new LivroDTO();
    }
    
    @Test
    @Order(1)
    public void authorization() {
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

        String content = given().spec(requestSpecification)
             .contentType(TestConfigs.CONTENT_TYPE_JSON)
             .accept(TestConfigs.CONTENT_TYPE_JSON)
             .body(livro)
             .when()
             	.post()
             .then()
             	.statusCode(200)
             .extract()
             	.body()
             		.asString();
        
        livro = objectMapper.readValue(content, LivroDTO.class);
        
        assertNotNull(livro);
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

        String content = given().spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .body(livro)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();
        
        LivroDTO livroAtualizado = objectMapper.readValue(content, LivroDTO.class);
        
        assertNotNull(livroAtualizado);
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
        String content = given().spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", livro.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
        
        LivroDTO livroBuscado = objectMapper.readValue(content, LivroDTO.class);
        
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", livro.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
    
    @Test
    @Order(6)
    public void testListarTodos() throws JsonMappingException, JsonProcessingException {
    	String content = given().spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 0 , "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
    	
        WrapperLivroDTO wrapperLivroDTO = objectMapper.readValue(content, WrapperLivroDTO.class);
        List<LivroDTO> livros = wrapperLivroDTO.getEmbedded().getLivros();
        
        LivroDTO livroUm = livros.get(0);
        
        assertNotNull(livroUm.getId());
        assertNotNull(livroUm.getTitulo());
        assertNotNull(livroUm.getAutor());
        assertNotNull(livroUm.getPreco());
        
        assertEquals(12, livroUm.getId());
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", livroUm.getTitulo());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", livroUm.getAutor());
        assertEquals(54.00, livroUm.getPreco());
        
        LivroDTO livroCinco = livros.get(4);
        
        assertNotNull(livroCinco.getId());
        assertNotNull(livroCinco.getTitulo());
        assertNotNull(livroCinco.getAutor());
        assertNotNull(livroCinco.getPreco());
        
        assertEquals(8, livroCinco.getId());
        assertEquals("Domain Driven Design", livroCinco.getTitulo());
        assertEquals("Eric Evans", livroCinco.getAutor());
        assertEquals(92.00, livroCinco.getPreco());
    }
    
    @Test
	@Order(7)
	public void testListarTodosSemToken() throws JsonMappingException, JsonProcessingException {
		RequestSpecification requestSpecificationSemToken = new RequestSpecBuilder()
		        .setBasePath("/api/livros/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given().spec(requestSpecificationSemToken)
	            .contentType(TestConfigs.CONTENT_TYPE_JSON)
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.when()
					.get()
				.then()
					.statusCode(403)
				.extract()
					.body()
						.asString();
	}
    
	@Test
	@Order(8)
	public void testHateoas() throws JsonMappingException, JsonProcessingException {
    	String content = given().spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 0 , "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
		
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/livros/v1/3\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/livros/v1/5\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/livros/v1/7\"}}}"));
		
		assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/livros/v1?direction=asc&page=0&size=12&sort=titulo,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/livros/v1?page=0&size=12&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/livros/v1?direction=asc&page=1&size=12&sort=titulo,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/livros/v1?direction=asc&page=1&size=12&sort=titulo,asc\"}}"));
		
		assertTrue(content.contains("\"page\":{\"size\":12,\"totalElements\":15,\"totalPages\":2,\"number\":0}}"));
	}
     
    private void mockLivro() {
        livro.setTitulo("Docker Deep Dive");
        livro.setAutor("Nigel Poulton");
        livro.setPreco(Double.valueOf(55.99));
        livro.setDataLancamento(new Date());
    } 
    
}