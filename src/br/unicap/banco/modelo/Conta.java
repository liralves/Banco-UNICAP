package br.unicap.banco.modelo;

import br.unicap.banco.excecoes.SaldoInsuficienteException;
import br.unicap.banco.excecoes.ContaInativaException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa uma conta bancária genérica.
 * Serve como base para ContaCorrente e ContaPoupanca.
 */
public abstract class Conta {

    private static int contadorNumero = 1000;

    protected int numero;
    protected double saldo;
    protected boolean ativa;
    protected Funcionario titular;
    protected List<Transacao> extrato;
    protected LocalDateTime dataCriacao;

    public Conta(Funcionario titular) {
        this.numero = ++contadorNumero;
        this.titular = titular;
        this.saldo = 0.0;
        this.ativa = true;
        this.extrato = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
    }

    // --- Métodos concretos comuns ---

    public void depositar(double valor) throws ContaInativaException {
        verificarAtiva();
        if (valor <= 0) throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        this.saldo += valor;
        registrarTransacao("DEPÓSITO", valor);
    }

    public void sacar(double valor) throws SaldoInsuficienteException, ContaInativaException {
        verificarAtiva();
        if (valor <= 0) throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        double limite = saldo + getLimiteEspecial();
        if (valor > limite) throw new SaldoInsuficienteException("Saldo insuficiente. Saldo disponível: R$ " + String.format("%.2f", limite));
        this.saldo -= valor;
        registrarTransacao("SAQUE", -valor);
    }

    public void transferir(Conta destino, double valor) throws SaldoInsuficienteException, ContaInativaException {
        verificarAtiva();
        destino.verificarAtiva();
        this.sacar(valor);
        destino.depositar(valor);
        // Ajusta o extrato da origem para indicar transferência
        extrato.get(extrato.size() - 1).setDescricao("TRANSFERÊNCIA ENVIADA → Conta " + destino.getNumero());
        destino.extrato.get(destino.extrato.size() - 1).setDescricao("TRANSFERÊNCIA RECEBIDA ← Conta " + this.numero);
    }

    public void encerrar() throws ContaInativaException {
        verificarAtiva();
        this.ativa = false;
        registrarTransacao("ENCERRAMENTO", 0);
    }

    protected void verificarAtiva() throws ContaInativaException {
        if (!ativa) throw new ContaInativaException("A conta " + numero + " está encerrada.");
    }

    protected void registrarTransacao(String descricao, double valor) {
        extrato.add(new Transacao(descricao, valor, this.saldo));
    }

    // --- Métodos abstratos (polimorfismo) ---

    /** Retorna o tipo da conta como texto. */
    public abstract String getTipoConta();

    /** Retorna o limite especial (ex: cheque especial). Padrão 0. */
    public double getLimiteEspecial() { return 0.0; }

    /** Aplica rendimento mensal se aplicável. */
    public abstract void aplicarRendimento();

    // --- Getters e Setters ---

    public int getNumero() { return numero; }
    public double getSaldo() { return saldo; }
    public boolean isAtiva() { return ativa; }
    public Funcionario getTitular() { return titular; }
    public List<Transacao> getExtrato() { return extrato; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    @Override
    public String toString() {
        return String.format("[%s] Conta #%d | Titular: %s | Saldo: R$ %.2f | %s",
                getTipoConta(), numero, titular.getNome(), saldo, ativa ? "ATIVA" : "ENCERRADA");
    }
}
