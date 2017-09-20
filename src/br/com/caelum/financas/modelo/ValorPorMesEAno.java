package br.com.caelum.financas.modelo;

import java.math.BigDecimal;

public class ValorPorMesEAno {

	private int ano;
	private int mes;
	BigDecimal valor;
	
	public ValorPorMesEAno(int ano, int mes, BigDecimal valor) {
		this.ano = ano;
		this.mes = mes;
		this.valor = valor;
	}

	public int getAno() {
		return ano;
	}

	public int getMes() {
		return mes;
	}

	public BigDecimal getValor() {
		return valor;
	}
	
	
}
