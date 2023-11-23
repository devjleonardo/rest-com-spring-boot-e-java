package com.joseleonardo.integrationtests.controller.withxml;

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
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.LivroDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.dto.pagedmodels.PagedModelLivro;
import com.joseleonardo.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class LivroControllerXmlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification requestSpecification;
	private static XmlMapper objectMapper;

	private static LivroDTO livro;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		livro = new LivroDTO();
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
		        .contentType(TestConfigs.CONTENT_TYPE_XML)
	            .accept(TestConfigs.CONTENT_TYPE_XML)
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
			    .contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(livro)
				.when()
				    .post()
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
			    .contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
	public void testDeletar() throws JsonMappingException, JsonProcessingException {
	    given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
		        .contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 0 , "size", 12, "direction", "asc")
				.when()
				    .get()
				.then()
				    .statusCode(200)
				.extract()
				    .body()
					    .asString();
		
		PagedModelLivro pagedModelLivro = objectMapper.readValue(content, PagedModelLivro.class);
		List<LivroDTO> livros = pagedModelLivro.getContent();
		
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
		        .contentType(TestConfigs.CONTENT_TYPE_XML)
	            .accept(TestConfigs.CONTENT_TYPE_XML)
				.when()
					.get()
				.then()
					.statusCode(403);
	}
	
	@Test
	@Order(8)
	public void testHateoas() throws JsonMappingException, JsonProcessingException {
		String content = given().spec(requestSpecification)
		        .contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 0 , "size", 12, "direction", "asc")
				.when()
				    .get()
				.then()
				    .statusCode(200)
				.extract()
				    .body()
					    .asString();
		
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/livros/v1/3</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/livros/v1/5</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/livros/v1/7</href></links>"));
		
		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/livros/v1?direction=asc&amp;page=0&amp;size=12&amp;sort=titulo,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/livros/v1?page=0&amp;size=12&amp;direction=asc</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/livros/v1?direction=asc&amp;page=1&amp;size=12&amp;sort=titulo,asc</href></links>"));
		assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/livros/v1?direction=asc&amp;page=1&amp;size=12&amp;sort=titulo,asc</href></links>"));
		
		assertTrue(content.contains("<page><size>12</size><totalElements>15</totalElements><totalPages>2</totalPages><number>0</number></page>"));
	}
	
    private void mockLivro() {
        livro.setTitulo("Docker Deep Dive");
        livro.setAutor("Nigel Poulton");
        livro.setPreco(Double.valueOf(55.99));
        livro.setDataLancamento(new Date());
    }  
}