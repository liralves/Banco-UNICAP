package br.unicap.banco.modelo;

/**
 * Interface que define o contrato de autenticação.
 * Implementada por qualquer entidade que precise de login.
 */
public interface Autenticavel {

    /**
     * Verifica se a senha fornecida é válida.
     * @param senha a senha a ser verificada
     * @return true se autenticado com sucesso
     */
    boolean autenticar(String senha);

    /**
     * Altera a senha da entidade.
     * @param novaSenha nova senha desejada
     */
    void alterarSenha(String novaSenha);
}
