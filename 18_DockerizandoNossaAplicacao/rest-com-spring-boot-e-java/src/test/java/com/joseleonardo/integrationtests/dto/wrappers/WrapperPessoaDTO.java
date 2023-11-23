package com.joseleonardo.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WrapperPessoaDTO")
public class WrapperPessoaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("_embedded")
	private PessoaEmbeddedDTO embedded;

	public WrapperPessoaDTO() {
	}

	public PessoaEmbeddedDTO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(PessoaEmbeddedDTO embedded) {
		this.embedded = embedded;
	}

	@Override
	public int hashCode() {
		return Objects.hash(embedded);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapperPessoaDTO other = (WrapperPessoaDTO) obj;
		return Objects.equals(embedded, other.embedded);
	}

}
