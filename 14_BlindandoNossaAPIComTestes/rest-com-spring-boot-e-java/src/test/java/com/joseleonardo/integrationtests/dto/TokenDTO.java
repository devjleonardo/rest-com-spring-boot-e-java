package com.joseleonardo.integrationtests.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TokenDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeDeUsuario;
	private Boolean autenticado;
	private Date dataCriacao;
	private Date expiraçao;
	private String accessToken;
	private String refreshToken;

	public TokenDTO() {
	}

	public TokenDTO(String nomeDeUsuario, Boolean autenticado, Date dataCriacao, 
			Date expiraçao, String accessToken, String refreshToken) {
		super();
		this.nomeDeUsuario = nomeDeUsuario;
		this.autenticado = autenticado;
		this.dataCriacao = dataCriacao;
		this.expiraçao = expiraçao;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getNomeDeUsuario() {
		return nomeDeUsuario;
	}

	public void setNomeDeUsuario(String nomeDeUsuario) {
		this.nomeDeUsuario = nomeDeUsuario;
	}

	public Boolean getAutenticado() {
		return autenticado;
	}

	public void setAutenticado(Boolean autenticado) {
		this.autenticado = autenticado;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getExpiraçao() {
		return expiraçao;
	}

	public void setExpiraçao(Date expiraçao) {
		this.expiraçao = expiraçao;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken, autenticado, dataCriacao, expiraçao, nomeDeUsuario, refreshToken);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDTO other = (TokenDTO) obj;
		return Objects.equals(accessToken, other.accessToken) && Objects.equals(autenticado, other.autenticado)
				&& Objects.equals(dataCriacao, other.dataCriacao) && Objects.equals(expiraçao, other.expiraçao)
				&& Objects.equals(nomeDeUsuario, other.nomeDeUsuario)
				&& Objects.equals(refreshToken, other.refreshToken);
	}

}
