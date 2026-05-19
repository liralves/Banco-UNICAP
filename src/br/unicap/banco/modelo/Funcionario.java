package br.unicap.banco.modelo;

/**
 * Representa um funcionário da UNICAP, que pode ser titular de contas bancárias.
 */
public class Funcionario {

    private static int contadorId = 100;

    private int id;
    private String nome;
    private String cpf;
    private String cargo;
    private String email;
    private String senhaHash; // senha criptografada simples

    public Funcionario(String nome, String cpf, String cargo, String email, String senha) {
        this.id = ++contadorId;
        this.nome = nome;
        this.cpf = cpf;
        this.cargo = cargo;
        this.email = email;
        this.senhaHash = gerarHash(senha);
    }

    /** Geração de hash simples (soma dos char codes). Em produção, use BCrypt. */
    private String gerarHash(String senha) {
        int hash = 0;
        for (char c : senha.toCharArray()) hash = hash * 31 + c;
        return Integer.toHexString(Math.abs(hash));
    }

    public boolean autenticar(String senha) {
        return this.senhaHash.equals(gerarHash(senha));
    }

    public void alterarSenha(String novaSenha) {
        this.senhaHash = gerarHash(novaSenha);
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getCargo() { return cargo; }
    public String getEmail() { return email; }

    // --- Setters ---
    public void setNome(String nome) { this.nome = nome; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | CPF: %s | Cargo: %s | Email: %s",
                id, nome, cpf, cargo, email);
    }
}
