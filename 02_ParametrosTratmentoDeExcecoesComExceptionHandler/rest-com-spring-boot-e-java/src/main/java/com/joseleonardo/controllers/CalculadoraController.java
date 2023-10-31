package com.joseleonardo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.conversores.ConversorDeNumero;
import com.joseleonardo.exceptions.UnsupportedMathOperationException;
import com.joseleonardo.math.OperacoesMatematicaSimples;

@RestController
@RequestMapping("/calculadora")
public class CalculadoraController {

	private OperacoesMatematicaSimples operacoesMatematica = new OperacoesMatematicaSimples(); 
	
	@GetMapping("/soma/{numeroUm}/{numeroDois}")
	public Double soma(
			@PathVariable(name = "numeroUm") String numeroUm,
			@PathVariable(name = "numeroDois") String numeroDois) throws Exception {
		
		if (!ConversorDeNumero.isNumero(numeroUm) || !ConversorDeNumero.isNumero(numeroDois)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		Double numeroUmConvertido = ConversorDeNumero.converterParaDouble(numeroUm);
		Double numeroDoisConvertido = ConversorDeNumero.converterParaDouble(numeroDois);
		
		return operacoesMatematica.soma(numeroUmConvertido, numeroDoisConvertido);
	}
	
	@GetMapping("/subtracao/{numeroUm}/{numeroDois}")
	public Double subtracao(
			@PathVariable(name = "numeroUm") String numeroUm,
			@PathVariable(name = "numeroDois") String numeroDois) throws Exception {
		
		if (!ConversorDeNumero.isNumero(numeroUm) || !ConversorDeNumero.isNumero(numeroDois)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		Double numeroUmConvertido = ConversorDeNumero.converterParaDouble(numeroUm);
		Double numeroDoisConvertido = ConversorDeNumero.converterParaDouble(numeroDois);
		
		return operacoesMatematica.subtracao(numeroUmConvertido, numeroDoisConvertido);
	}
	
	@GetMapping("/multiplicacao/{numeroUm}/{numeroDois}")
	public Double multiplicacao(
			@PathVariable(name = "numeroUm") String numeroUm,
			@PathVariable(name = "numeroDois") String numeroDois) throws Exception {
		
		if (!ConversorDeNumero.isNumero(numeroUm) || !ConversorDeNumero.isNumero(numeroDois)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		Double numeroUmConvertido = ConversorDeNumero.converterParaDouble(numeroUm);
		Double numeroDoisConvertido = ConversorDeNumero.converterParaDouble(numeroDois);
		
		return operacoesMatematica.multiplicacao(numeroUmConvertido, numeroDoisConvertido);
	}
	
	@GetMapping("/divisao/{numeroUm}/{numeroDois}")
	public Double divisao(
			@PathVariable(name = "numeroUm") String numeroUm,
			@PathVariable(name = "numeroDois") String numeroDois) throws Exception {
		
		if (!ConversorDeNumero.isNumero(numeroUm) || !ConversorDeNumero.isNumero(numeroDois)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		Double numeroUmConvertido = ConversorDeNumero.converterParaDouble(numeroUm);
		Double numeroDoisConvertido = ConversorDeNumero.converterParaDouble(numeroDois);
		
		return operacoesMatematica.divisao(numeroUmConvertido, numeroDoisConvertido);
	}
	
	@GetMapping("/media/{numeroUm}/{numeroDois}")
	public Double media(
			@PathVariable(name = "numeroUm") String numeroUm,
			@PathVariable(name = "numeroDois") String numeroDois) throws Exception {
		
		if (!ConversorDeNumero.isNumero(numeroUm) || !ConversorDeNumero.isNumero(numeroDois)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		Double numeroUmConvertido = ConversorDeNumero.converterParaDouble(numeroUm);
		Double numeroDoisConvertido = ConversorDeNumero.converterParaDouble(numeroDois);
		
		return operacoesMatematica.media(numeroUmConvertido, numeroDoisConvertido);
	}
	
	@GetMapping("/raiz-quadrada/{numero}")
	public Double raizQuadrada(
			@PathVariable(name = "numero") String numero) throws Exception {
		
		if (!ConversorDeNumero.isNumero(numero)) {
			throw new UnsupportedMathOperationException("Por favor digite um valor númerico");
		}
		
		Double numeroConvertido = ConversorDeNumero.converterParaDouble(numero);
		
		return operacoesMatematica.raizQuadrada(numeroConvertido);
	}
	
}
