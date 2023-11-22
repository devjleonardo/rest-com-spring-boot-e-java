package com.joseleonardo.integrationtests.controller.withyaml;

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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joseleonardo.configs.TestConfigs;
import com.joseleonardo.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.joseleonardo.integrationtests.dto.CredenciaisDaContaDTO;
import com.joseleonardo.integrationtests.dto.LivroDTO;
import com.joseleonardo.integrationtests.dto.TokenDTO;
import com.joseleonardo.integrationtests.dto.pagedmodels.PagedModelLivro;
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
    	PagedModelLivro pagedModelLivro = given().spec(requestSpecification)
                .config(
                    RestAssuredConfig
                        .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(
                                    TestConfigs.CONTENT_TYPE_YML, 
                                    ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
		        .accept(TestConfigs.CONTENT_TYPE_YML)
		        .queryParams("page", 0 , "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .as(PagedModelLivro.class, ymlMapper); 

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
	
	@Test
	@Order(8)
	public void testHateoas() throws JsonMappingException, JsonProcessingException {
    	String unthreatedContent = given().spec(requestSpecification)
                .config(
                    RestAssuredConfig
                        .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(
                                    TestConfigs.CONTENT_TYPE_YML, 
                                    ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
		        .accept(TestConfigs.CONTENT_TYPE_YML)
		        .queryParams("page", 0 , "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString(); 
		
		String content = unthreatedContent.replace("\n", "").replace("\r", "");
		
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/livros/v1/3\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/livros/v1/5\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/livros/v1/7\""));
		
		assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8888/api/livros/v1?direction=asc&page=0&size=12&sort=titulo,asc\""));
		assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8888/api/livros/v1?page=0&size=12&direction=asc\""));
		assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8888/api/livros/v1?direction=asc&page=1&size=12&sort=titulo,asc\""));
		assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8888/api/livros/v1?direction=asc&page=1&size=12&sort=titulo,asc\""));
		
		assertTrue(content.contains("page:  size: 12  totalElements: 15  totalPages: 2  number: 0"));
	}
     
    private void mockLivro() {
        livro.setTitulo("Docker Deep Dive");
        livro.setAutor("Nigel Poulton");
        livro.setPreco(Double.valueOf(55.99));
        livro.setDataLancamento(new Date());
    }
}