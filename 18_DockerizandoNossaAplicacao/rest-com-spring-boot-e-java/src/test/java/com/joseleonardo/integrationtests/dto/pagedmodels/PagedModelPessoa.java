package com.joseleonardo.integrationtests.dto.pagedmodels;

import java.util.List;

import com.joseleonardo.integrationtests.dto.PessoaDTO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PagedModelPessoa")
public class PagedModelPessoa {

	@XmlElement(name = "content")
	private List<PessoaDTO> content;

	public PagedModelPessoa() {
	}

	public List<PessoaDTO> getContent() {
		return content;
	}

	public void setContent(List<PessoaDTO> content) {
		this.content = content;
	}

}
