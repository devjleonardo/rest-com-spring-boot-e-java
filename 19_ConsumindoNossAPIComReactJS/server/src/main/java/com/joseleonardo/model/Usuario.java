package com.joseleonardo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class Usuario implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_name", unique = true)
	private String nomeDeUsuario;
	
	@Column(name = "full_name")
	private String nomeCompleto;
	
	@Column(name = "password")
	private String senha;
	
	@Column(name = "account_non_expired")
	private Boolean contaNaoExpirada;
	
	@Column(name = "account_non_locked")
	private Boolean contaNaoBloqueada;
	
	@Column(name = "credentials_non_expired")
	private Boolean credenciaisNaoExpiradas;
	
	@Column(name = "enabled")
	private Boolean habilitado;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_permission",
	        joinColumns = @JoinColumn(name = "id_user"),
	        inverseJoinColumns = @JoinColumn(name = "id_permission"))
	private List<Permissao> permissoes;
	
	public Usuario() {
	}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();

		for (Permissao permissao : permissoes) {
			roles.add(permissao.getDescricao());
		}
		
		return roles ;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissoes;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return nomeDeUsuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		return contaNaoExpirada;
	}

	@Override
	public boolean isAccountNonLocked() {
		return contaNaoBloqueada;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credenciaisNaoExpiradas;
	}

	@Override
	public boolean isEnabled() {
		return habilitado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDeUsuario() {
		return nomeDeUsuario;
	}

	public void setNomeDeUsuario(String nomeDeUsuario) {
		this.nomeDeUsuario = nomeDeUsuario;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getContaNaoExpirada() {
		return contaNaoExpirada;
	}

	public void setContaNaoExpirada(Boolean contaNaoExpirada) {
		this.contaNaoExpirada = contaNaoExpirada;
	}

	public Boolean getContaNaoBloqueada() {
		return contaNaoBloqueada;
	}

	public void setContaNaoBloqueada(Boolean contaNaoBloqueada) {
		this.contaNaoBloqueada = contaNaoBloqueada;
	}

	public Boolean getCredenciaisNaoExpiradas() {
		return credenciaisNaoExpiradas;
	}

	public void setCredenciaisNaoExpiradas(Boolean credenciaisNaoExpiradas) {
		this.credenciaisNaoExpiradas = credenciaisNaoExpiradas;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contaNaoBloqueada, contaNaoExpirada, credenciaisNaoExpiradas, habilitado, id, nomeCompleto,
				nomeDeUsuario, permissoes, senha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(contaNaoBloqueada, other.contaNaoBloqueada)
				&& Objects.equals(contaNaoExpirada, other.contaNaoExpirada)
				&& Objects.equals(credenciaisNaoExpiradas, other.credenciaisNaoExpiradas)
				&& Objects.equals(habilitado, other.habilitado) && Objects.equals(id, other.id)
				&& Objects.equals(nomeCompleto, other.nomeCompleto)
				&& Objects.equals(nomeDeUsuario, other.nomeDeUsuario) && Objects.equals(permissoes, other.permissoes)
				&& Objects.equals(senha, other.senha);
	}
	
}
