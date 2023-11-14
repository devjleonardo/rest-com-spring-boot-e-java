package com.joseleonardo.data.dto.v1.security;

import java.io.Serializable;
import java.util.Objects;

public class CredenciaisDaContaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeDeUsuario;
	private String senha;
	
	public CredenciaisDaContaDTO() {
	}

	public CredenciaisDaContaDTO(String nomeDeUsuario, String senha) {
		this.nomeDeUsuario = nomeDeUsuario;
		this.senha = senha;
	}

	public String getNomeDeUsuario() {
		return nomeDeUsuario;
	}

	public void setNomeDeUsuario(String nomeDeUsuario) {
		this.nomeDeUsuario = nomeDeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nomeDeUsuario, senha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CredenciaisDaContaDTO other = (CredenciaisDaContaDTO) obj;
		return Objects.equals(nomeDeUsuario, other.nomeDeUsuario) && Objects.equals(senha, other.senha);
	}

}
