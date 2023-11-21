package com.joseleonardo.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joseleonardo.integrationtests.dto.PessoaDTO;

public class PessoaEmbeddedDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("pessoaDTOList")
	private List<PessoaDTO> pessoas;

	public PessoaEmbeddedDTO() {
	}

	public List<PessoaDTO> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<PessoaDTO> pessoas) {
		this.pessoas = pessoas;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pessoas);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaEmbeddedDTO other = (PessoaEmbeddedDTO) obj;
		return Objects.equals(pessoas, other.pessoas);
	}

}
