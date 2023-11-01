package com.joseleonardo.data.dto.v2;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PessoaDTOV2 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String primeiroNome;
	private String ultimoNome;
	private String endereco;
	private String genero;
	private Date dataDeNascimento;

	public PessoaDTOV2() {
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

	public Date getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(Date dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataDeNascimento, endereco, genero, id, primeiroNome, ultimoNome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaDTOV2 other = (PessoaDTOV2) obj;
		return Objects.equals(dataDeNascimento, other.dataDeNascimento) && Objects.equals(endereco, other.endereco)
				&& Objects.equals(genero, other.genero) && Objects.equals(id, other.id)
				&& Objects.equals(primeiroNome, other.primeiroNome) && Objects.equals(ultimoNome, other.ultimoNome);
	}

}
