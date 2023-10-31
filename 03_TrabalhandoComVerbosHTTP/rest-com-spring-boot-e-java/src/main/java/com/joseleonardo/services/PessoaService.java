package com.joseleonardo.services;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.joseleonardo.model.Pessoa;

@Service
public class PessoaService {

	private final AtomicLong contador = new AtomicLong();
	private Logger logger = Logger.getLogger(PessoaService.class.getName());
	
	public Pessoa buscarPorId(String id) {
		logger.info("Encontrar uma pessoa!");
		
		Pessoa pessoa = new Pessoa();
		pessoa.setId(contador.incrementAndGet());
		pessoa.setPrimeiroNome("Yuri");
		pessoa.setUltimoNome("Alexandre");
		pessoa.setEndereco("Uberlândia - Minas Gerais - Brasil");
		pessoa.setGenero("Masculino");
		
		return pessoa;
	}
	
}
