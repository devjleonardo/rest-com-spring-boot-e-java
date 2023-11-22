package com.joseleonardo.data.dto.v1;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class PessoaDTO extends RepresentationModel<PessoaDTO> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String primeiroNome;
	private String ultimoNome;
	private String endereco;
	private String genero;
	private Boolean habilitada;

	public PessoaDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimeiroNome() {
		return primeiroNome;
	}

	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
	}

	public String getUltimoNome() {
		return ultimoNome;
	}

	public void setUltimoNome(String ultimoNome) {
		this.ultimoNome = ultimoNome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Boolean getHabilitada() {
		return habilitada;
	}

	public void setHabilitada(Boolean habilitada) {
		this.habilitada = habilitada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(endereco, genero, habilitada, id, primeiroNome, ultimoNome);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaDTO other = (PessoaDTO) obj;
		return Objects.equals(endereco, other.endereco) && Objects.equals(genero, other.genero)
				&& Objects.equals(habilitada, other.habilitada) && Objects.equals(id, other.id)
				&& Objects.equals(primeiroNome, other.primeiroNome) && Objects.equals(ultimoNome, other.ultimoNome);
	}

}
