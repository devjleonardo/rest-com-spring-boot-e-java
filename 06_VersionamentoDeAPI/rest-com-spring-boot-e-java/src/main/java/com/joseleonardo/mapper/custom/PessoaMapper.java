package com.joseleonardo.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.joseleonardo.data.dto.v2.PessoaDTOV2;
import com.joseleonardo.model.Pessoa;

@Service
public class PessoaMapper {

	public PessoaDTOV2 converterEntityParaDTOV2(Pessoa entity) {
		PessoaDTOV2 dtoV2 = new PessoaDTOV2();
		dtoV2.setId(entity.getId());
		dtoV2.setPrimeiroNome(entity.getPrimeiroNome());
		dtoV2.setUltimoNome(entity.getUltimoNome());
		dtoV2.setEndereco(entity.getEndereco());
		dtoV2.setGenero(entity.getGenero());
		dtoV2.setDataDeNascimento(new Date());
		
		return dtoV2;
	}
	
	public Pessoa converterDTOV2ParaEntity(PessoaDTOV2 dtoV2) {
		Pessoa entity = new Pessoa();
		entity.setId(dtoV2.getId());
		entity.setPrimeiroNome(dtoV2.getPrimeiroNome());
		entity.setUltimoNome(dtoV2.getUltimoNome());
		entity.setEndereco(dtoV2.getEndereco());
		entity.setGenero(dtoV2.getGenero());
		// entity.setDataDeNascimento(new Date());
		
		return entity;
	}
	
}
