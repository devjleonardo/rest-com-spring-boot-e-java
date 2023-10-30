package com.joseleonardo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saudacoes")
public class SaudacaoController {

	private static final String TEMPLATE = "Hello, %s!";
	private static final AtomicLong CONTADOR = new AtomicLong();
	
	@GetMapping
	public Saudacao saudacao(@RequestParam(value = "nome", defaultValue = "World") String nome) {
		return new Saudacao(CONTADOR.incrementAndGet(), String.format(TEMPLATE, nome));
	}
	
}
