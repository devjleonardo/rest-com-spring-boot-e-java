package com.joseleonardo.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joseleonardo.data.dto.v1.LivroDTO;
import com.joseleonardo.model.Livro;

public class MockLivro {
	
    public Livro mockEntity() {
        return mockEntity(0);
    }
    
    public LivroDTO mockVO() {
        return mockVO(0);
    }
    
    public List<Livro> mockEntityList() {
        List<Livro> livros = new ArrayList<Livro>();
        
        for (int i = 0; i < 14; i++) {
            livros.add(mockEntity(i));
        }
        
        return livros;
    }

    public List<LivroDTO> mockVOList() {
        List<LivroDTO> livros = new ArrayList<>();
        
        for (int i = 0; i < 14; i++) {
            livros.add(mockVO(i));
        }
        
        return livros;
    }
    
    public Livro mockEntity(Integer numero) {
        Livro livro = new Livro();
        livro.setId(numero.longValue());
        livro.setAutor("Algum Autor" + numero);
        livro.setDataLancamento(new Date());
        livro.setPreco(25D);
        livro.setTitulo("Algum Título" + numero);
        
        return livro;
    }

    public LivroDTO mockVO(Integer numero) {
        LivroDTO livro = new LivroDTO();
        livro.setId(numero.longValue());
        livro.setAutor("Algum Autor" + numero);
        livro.setDataLancamento(new Date());
        livro.setPreco(25D);
        livro.setTitulo("Algum Título" + numero);
        
        return livro;
    }

}
