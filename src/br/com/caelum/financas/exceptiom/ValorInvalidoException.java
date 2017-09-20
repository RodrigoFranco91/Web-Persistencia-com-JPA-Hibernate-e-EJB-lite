package br.com.caelum.financas.exceptiom;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class ValorInvalidoException extends RuntimeException {

	public ValorInvalidoException(String message) {
		super(message);
	}
}
