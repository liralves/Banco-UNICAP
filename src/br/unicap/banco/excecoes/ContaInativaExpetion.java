package br.unicap.banco.excecoes;

/** Lançada ao tentar operar em uma conta encerrada. */
public class ContaInativaException extends Exception {
    public ContaInativaException(String mensagem) {
        super(mensagem);
    }
}
