package br.unicap.banco.excecoes;

/** Lançada quando a autenticação falha (login ou senha incorretos). */
public class AutenticacaoException extends Exception {
    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
