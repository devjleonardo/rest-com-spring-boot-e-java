package com.joseleonardo.conversores;

public class ConversorDeNumero {

	public static Double converterParaDouble(String stringDoNumero) {
		if (stringDoNumero == null) {
			return 0D;
		}
		
		String numero = stringDoNumero.replaceAll(",", ".");
		
		if (isNumero(numero)) {
			return Double.parseDouble(numero);
		}
		
		return 0D;
	}

	public static boolean isNumero(String stringDoNumero) {
		if (stringDoNumero == null) {
			return false;
		}
		
		String numero = stringDoNumero.replaceAll(",", ".");
		
		return numero.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
	
}
