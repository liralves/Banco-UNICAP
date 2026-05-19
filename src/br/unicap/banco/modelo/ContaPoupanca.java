package br.unicap.banco.modelo;

/**
 * Conta Poupança com rendimento mensal automático.
 * Herda de Conta e implementa rendimento de 0,5% ao mês.
 */
public class ContaPoupanca extends Conta {

    private static final double TAXA_RENDIMENTO = 0.005; // 0,5% ao mês
    private int diaAniversario; // dia do mês que rende

    public ContaPoupanca(Funcionario titular) {
        super(titular);
        this.diaAniversario = dataCriacao.getDayOfMonth();
    }

    @Override
    public String getTipoConta() {
        return "POUPANÇA";
    }

    /**
     * Aplica rendimento de 0,5% sobre o saldo atual.
     * Deve ser chamado mensalmente no dia aniversário.
     */
    @Override
    public void aplicarRendimento() {
        if (saldo > 0) {
            double rendimento = saldo * TAXA_RENDIMENTO;
            saldo += rendimento;
            registrarTransacao(String.format("RENDIMENTO (%.1f%%)", TAXA_RENDIMENTO * 100), rendimento);
        }
    }

    public int getDiaAniversario() { return diaAniversario; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Dia Aniversário: %d | Taxa: %.1f%%",
                diaAniversario, TAXA_RENDIMENTO * 100);
    }
}
