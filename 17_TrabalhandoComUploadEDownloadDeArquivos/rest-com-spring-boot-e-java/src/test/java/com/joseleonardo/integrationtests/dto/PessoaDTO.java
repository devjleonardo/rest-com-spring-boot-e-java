package com.joseleonardo.integrationtests.dto;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PessoaDTO")
public class PessoaDTO implements Serializable {

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
		return Objects.hash(endereco, genero, habilitada, id, primeiroNome, ultimoNome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaDTO other = (PessoaDTO) obj;
		return Objects.equals(endereco, other.endereco) && Objects.equals(genero, other.genero)
				&& Objects.equals(habilitada, other.habilitada) && Objects.equals(id, other.id)
				&& Objects.equals(primeiroNome, other.primeiroNome) && Objects.equals(ultimoNome, other.ultimoNome);
	}

}
