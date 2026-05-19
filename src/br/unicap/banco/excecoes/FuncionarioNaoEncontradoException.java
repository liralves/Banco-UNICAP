package br.unicap.banco.excecoes;

/** Lançada quando um funcionário não é encontrado no sistema. */
public class FuncionarioNaoEncontradoException extends Exception {
    public FuncionarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
