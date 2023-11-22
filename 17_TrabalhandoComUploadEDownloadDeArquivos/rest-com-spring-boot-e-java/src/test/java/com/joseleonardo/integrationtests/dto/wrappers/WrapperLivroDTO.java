package com.joseleonardo.integrationtests.dto.wrappers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperLivroDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("_embedded")
	private LivroEmbeddedDTO embedded;

	public WrapperLivroDTO() {
	}

	public LivroEmbeddedDTO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(LivroEmbeddedDTO embedded) {
		this.embedded = embedded;
	}

}
