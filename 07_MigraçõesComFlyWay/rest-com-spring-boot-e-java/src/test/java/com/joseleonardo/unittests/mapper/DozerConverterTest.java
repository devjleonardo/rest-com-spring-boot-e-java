package com.joseleonardo.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.joseleonardo.data.dto.v1.PessoaDTO;
import com.joseleonardo.mapper.DozerMapper;
import com.joseleonardo.model.Pessoa;
import com.joseleonardo.unittests.mapper.mocks.MockPerson;

public class DozerConverterTest {
    
    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToDTOTest() {
        PessoaDTO output = DozerMapper.parseObject(inputObject.mockEntity(), PessoaDTO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Primeiro nome teste0", output.getPrimeiroNome());
        assertEquals("Último nome teste0", output.getUltimoNome());
        assertEquals("Endereço teste0", output.getEndereco());
        assertEquals("Masculino", output.getGenero());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<PessoaDTO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), PessoaDTO.class);
       
        PessoaDTO outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Primeiro nome teste0", outputZero.getPrimeiroNome());
        assertEquals("Último nome teste0", outputZero.getUltimoNome());
        assertEquals("Endereço teste0", outputZero.getEndereco());
        assertEquals("Masculino", outputZero.getGenero());
        
        PessoaDTO outputSete = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSete.getId());
        assertEquals("Primeiro nome teste7", outputSete.getPrimeiroNome());
        assertEquals("Último nome teste7", outputSete.getUltimoNome());
        assertEquals("Endereço teste7", outputSete.getEndereco());
        assertEquals("Feminino", outputSete.getGenero());
        
        PessoaDTO outputDoze = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputDoze.getId());
        assertEquals("Primeiro nome teste12", outputDoze.getPrimeiroNome());
        assertEquals("Último nome teste12", outputDoze.getUltimoNome());
        assertEquals("Endereço teste12", outputDoze.getEndereco());
        assertEquals("Masculino", outputDoze.getGenero());
    }

    @Test
    public void parseDTOToEntityTest() {
        Pessoa output = DozerMapper.parseObject(inputObject.mockVO(), Pessoa.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Primeiro nome teste0", output.getPrimeiroNome());
        assertEquals("Último nome teste0", output.getUltimoNome());
        assertEquals("Endereço teste0", output.getEndereco());
        assertEquals("Masculino", output.getGenero());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Pessoa> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Pessoa.class);

        Pessoa outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Primeiro nome teste0", outputZero.getPrimeiroNome());
        assertEquals("Último nome teste0", outputZero.getUltimoNome());
        assertEquals("Endereço teste0", outputZero.getEndereco());
        assertEquals("Masculino", outputZero.getGenero());
        
        Pessoa outputSete = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSete.getId());
        assertEquals("Primeiro nome teste7", outputSete.getPrimeiroNome());
        assertEquals("Último nome teste7", outputSete.getUltimoNome());
        assertEquals("Endereço teste7", outputSete.getEndereco());
        assertEquals("Feminino", outputSete.getGenero());
        
        Pessoa outputDoze = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputDoze.getId());
        assertEquals("Primeiro nome teste12", outputDoze.getPrimeiroNome());
        assertEquals("Último nome teste12", outputDoze.getUltimoNome());
        assertEquals("Endereço teste12", outputDoze.getEndereco());
        assertEquals("Masculino", outputDoze.getGenero());
    }
    
}
