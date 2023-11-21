package com.joseleonardo.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joseleonardo.integrationtests.dto.LivroDTO;

public class LivroEmbeddedDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("livroDTOList")
	private List<LivroDTO> livros;
	
	public LivroEmbeddedDTO() {
	}

	public List<LivroDTO> getLivros() {
		return livros;
	}

	public void setLivros(List<LivroDTO> livros) {
		this.livros = livros;
	}

}
