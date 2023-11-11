package com.joseleonardo.data.dto.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class LivroDTO extends RepresentationModel<LivroDTO> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String autor;
	private Date dataLancamento;
	private Double preco;
	private String titulo;

	public LivroDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(autor, dataLancamento, id, preco, titulo);
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
		LivroDTO other = (LivroDTO) obj;
		return Objects.equals(autor, other.autor) && Objects.equals(dataLancamento, other.dataLancamento)
				&& Objects.equals(id, other.id) && Objects.equals(preco, other.preco)
				&& Objects.equals(titulo, other.titulo);
	}

}
