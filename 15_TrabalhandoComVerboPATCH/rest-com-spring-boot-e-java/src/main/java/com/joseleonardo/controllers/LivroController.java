package com.joseleonardo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.data.dto.v1.LivroDTO;
import com.joseleonardo.services.LivroService;
import com.joseleonardo.util.MediaTypeUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/livros/v1")
@Tag(name = "Livros", description = "Endpoints para gerenciamento de livros")
public class LivroController {

	@Autowired
	private LivroService livroService;
	
	@GetMapping(produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
			MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Listar todos livros", 
	    description = "Listar todos livros", 
	    tags = {"Livros"},
	    responses = {
		    @ApiResponse(description = "Sucesso", responseCode = "200", 
			    content = {
				    @Content(
					    mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = LivroDTO.class))
					)
				}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public List<LivroDTO> listar() {
		return livroService.listarTodos();
	}
	
	@GetMapping(value = "/{id}", 
			produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
					MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Buscar um livro", 
	    description = "Buscar um livro", 
	    tags = {"Livros"},
	    responses = {
		    @ApiResponse(description = "Sucesso", responseCode = "200", 
		        content = @Content(schema = @Schema(implementation = LivroDTO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public LivroDTO buscarPorId(@PathVariable(value = "id") Long id) {
		return livroService.buscarPorId(id);
	}
	
	@PostMapping(
			consumes = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML,
					MediaTypeUtil.APPLICATION_YML }, 
			produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML,
					MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Adicionar um novo livro", 
	    description = "Adiciona um novo Livro passando uma representação JSON, XML ou YML do livro!", 
	    tags = {"Livros"},
        responses = {
	        @ApiResponse(description = "Sucesso", responseCode = "200", 
	            content = @Content(schema = @Schema(implementation = LivroDTO.class))
		    ),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	    }
	)
	public LivroDTO salvar(@RequestBody LivroDTO pessoa) {
		return livroService.salvar(pessoa);
	}
	
	@PutMapping(
			consumes = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
					MediaTypeUtil.APPLICATION_YML }, 
			produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
					MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Atualizar um livro", 
        description = "Atualiza um Livro passando uma representação JSON, XML ou YML do livro!", 
        tags = {"Livros"},
        responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200", 
                content = @Content(schema = @Schema(implementation = LivroDTO.class))
	        ),
	        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
	        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
	        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
	public LivroDTO atualizar(@RequestBody LivroDTO pessoa) {
		return livroService.atualizar(pessoa);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletar um livro", 
        description = "Deletar um livro", 
        tags = {"Livros"},
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Long id) {
		livroService.deletar(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
