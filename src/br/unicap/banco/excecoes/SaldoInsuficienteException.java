package br.unicap.banco.excecoes;

/** Lançada quando o saldo é insuficiente para a operação. */
public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
