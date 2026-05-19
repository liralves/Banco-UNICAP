package br.unicap.banco.modelo;

/**
 * Usuário do sistema bancário. Vincula um Funcionário a credenciais de acesso.
 * Implementa a interface Autenticavel.
 */
public class Usuario implements Autenticavel {

    private String login;
    private String senhaHash;
    private Funcionario funcionario;
    private boolean ativo;

    public Usuario(String login, String senha, Funcionario funcionario) {
        this.login = login;
        this.senhaHash = gerarHash(senha);
        this.funcionario = funcionario;
        this.ativo = true;
    }

    private String gerarHash(String senha) {
        int hash = 0;
        for (char c : senha.toCharArray()) hash = hash * 31 + c;
        return Integer.toHexString(Math.abs(hash));
    }

    @Override
    public boolean autenticar(String senha) {
        return ativo && senhaHash.equals(gerarHash(senha));
    }

    @Override
    public void alterarSenha(String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 4)
            throw new IllegalArgumentException("A senha deve ter no mínimo 4 caracteres.");
        this.senhaHash = gerarHash(novaSenha);
    }

    public String getLogin() { return login; }
    public Funcionario getFuncionario() { return funcionario; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Usuário: " + login + " | Funcionário: " + funcionario.getNome();
    }
}
