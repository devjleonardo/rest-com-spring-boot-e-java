package com.joseleonardo.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.model.Pessoa;

public class MockPerson {

    public Pessoa mockEntity() {
        return mockEntity(0);
    }
    
    public PessoaDTO mockVO() {
        return mockVO(0);
    }
    
    public List<Pessoa> mockEntityList() {
        List<Pessoa> pessoas = new ArrayList<Pessoa>();
        
        for (int i = 0; i < 14; i++) {
            pessoas.add(mockEntity(i));
        }
        
        return pessoas;
    }

    public List<PessoaDTO> mockVOList() {
        List<PessoaDTO> pessoas = new ArrayList<>();
        
        for (int i = 0; i < 14; i++) {
            pessoas.add(mockVO(i));
        }
        
        return pessoas;
    }
    
    public Pessoa mockEntity(Integer numero) {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(numero.longValue());
        pessoa.setPrimeiroNome("Primeiro nome teste" + numero);
        pessoa.setUltimoNome("Último nome teste" + numero);
        pessoa.setGenero(((numero % 2) == 0) ? "Masculino" : "Feminino");
        pessoa.setEndereco("Endereço teste" + numero);
        
        return pessoa;
    }

    public PessoaDTO mockVO(Integer numero) {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setId(numero.longValue());
        pessoaDTO.setPrimeiroNome("Primeiro nome teste" + numero);
        pessoaDTO.setUltimoNome("Último nome teste" + numero);
        pessoaDTO.setGenero(((numero % 2) == 0) ? "Masculino" : "Feminino");
        pessoaDTO.setEndereco("Endereço teste" + numero);
        
        return pessoaDTO;
    }

}