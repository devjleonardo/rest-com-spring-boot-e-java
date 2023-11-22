package com.joseleonardo.integrationtests.dto.pagedmodels;

import java.util.List;

import com.joseleonardo.integrationtests.dto.LivroDTO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelLivro {

	@XmlElement(name = "content")
	private List<LivroDTO> content;

	public PagedModelLivro() {
	}

	public List<LivroDTO> getContent() {
		return content;
	}

	public void setContent(List<LivroDTO> content) {
		this.content = content;
	}

}