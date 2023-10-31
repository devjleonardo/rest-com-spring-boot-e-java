package com.joseleonardo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.exceptions.UnsupportedMathOperationException;

@RestController
@RequestMapping("/calculadora")
public class CalculadoraController {

	private static final AtomicLong CONTADOR = new AtomicLong();
	
	@GetMapping("/soma/{numeroUm}/{numeroDois}")
	public Double soma(
			@PathVariable(name = "numeroUm") String numeroUm,
			@PathVariable(name = "numeroDois") String numeroDois) throws Exception {
		if (!isNumero(numeroUm) || !isNumero(numeroDois)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		return converterParaDouble(numeroUm) + converterParaDouble(numeroDois);
	}

	private Double converterParaDouble(String stringDoNumero) {
		if (stringDoNumero == null) {
			return 0D;
		}
		
		String numero = stringDoNumero.replaceAll(",", ".");
		
		if (isNumero(numero)) {
			return Double.parseDouble(numero);
		}
		
		return 0D;
	}

	private boolean isNumero(String stringDoNumero) {
		if (stringDoNumero == null) {
			return false;
		}
		
		String numero = stringDoNumero.replaceAll(",", ".");
		
		return numero.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
	
}
