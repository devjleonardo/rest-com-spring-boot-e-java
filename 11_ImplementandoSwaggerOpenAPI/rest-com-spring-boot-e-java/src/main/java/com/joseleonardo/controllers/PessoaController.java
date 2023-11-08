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

import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.services.PessoaService;
import com.joseleonardo.util.MediaTypeUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pessoas/v1")
@Tag(name = "Pessoas", description = "Endpoints para gerenciamento de pessoas")
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;
	
	@GetMapping(produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
			MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Listar todas pessoas", 
	    description = "Listar todas pessoas", 
	    tags = {"Pessoas"},
	    responses = {
		    @ApiResponse(description = "Sucesso", responseCode = "200", 
			    content = {
				    @Content(
					    mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = PessoaDTO.class))
					)
				}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public List<PessoaDTO> listar() {
		return pessoaService.listarTodas();
	}
	
	@GetMapping(value = "/{id}", 
			produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
					MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Buscar uma pessoa", 
	    description = "Buscar uma pessoa", 
	    tags = {"Pessoas"},
	    responses = {
		    @ApiResponse(description = "Sucesso", responseCode = "200", 
		        content = @Content(schema = @Schema(implementation = PessoaDTO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public PessoaDTO buscarPorId(@PathVariable(value = "id") Long id) {
		return pessoaService.buscarPorId(id);
	}
	
	@PostMapping(
			consumes = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML,
					MediaTypeUtil.APPLICATION_YML }, 
			produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML,
					MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Adicionar uma nova pessoa", 
	    description = "Adiciona uma nova Pessoa passando uma representação JSON, XML ou YML da pessoa!", 
	    tags = {"Pessoas"},
        responses = {
	        @ApiResponse(description = "Sucesso", responseCode = "200", 
	            content = @Content(schema = @Schema(implementation = PessoaDTO.class))
		    ),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	    }
	)
	public PessoaDTO salvar(@RequestBody PessoaDTO pessoa) {
		return pessoaService.salvar(pessoa);
	}
	
	@PutMapping(
			consumes = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
					MediaTypeUtil.APPLICATION_YML }, 
			produces = { MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, 
					MediaTypeUtil.APPLICATION_YML })
	@Operation(summary = "Atualizar uma pessoa", 
        description = "Atualizar uma Pessoa passando uma representação JSON, XML ou YML da pessoa!", 
        tags = {"Pessoas"},
        responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200", 
                content = @Content(schema = @Schema(implementation = PessoaDTO.class))
	        ),
	        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
	        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
	        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
	public PessoaDTO atualizar(@RequestBody PessoaDTO pessoa) {
		return pessoaService.atualizar(pessoa);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletar uma pessoa", 
        description = "Deletar uma pessoa", 
        tags = {"Pessoas"},
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Long id) {
		pessoaService.deletar(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
