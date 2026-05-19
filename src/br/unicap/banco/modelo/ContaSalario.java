package br.unicap.banco.modelo;

import br.unicap.banco.excecoes.SaldoInsuficienteException;
import br.unicap.banco.excecoes.ContaInativaException;

/**
 * Conta Salário: recebe crédito de salário e permite apenas 1 saque gratuito por mês.
 * Saques adicionais têm cobrança de R$ 3,50 por operação.
 */
public class ContaSalario extends Conta {

    private static final double TAXA_SAQUE_EXTRA = 3.50;
    private int saquesMesAtual;

    public ContaSalario(Funcionario titular) {
        super(titular);
        this.saquesMesAtual = 0;
    }

    @Override
    public String getTipoConta() {
        return "SALÁRIO";
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException, ContaInativaException {
        verificarAtiva();
        if (valor <= 0) throw new IllegalArgumentException("Valor de saque deve ser positivo.");

        double taxa = (saquesMesAtual >= 1) ? TAXA_SAQUE_EXTRA : 0.0;
        double totalDebitar = valor + taxa;

        if (totalDebitar > saldo) throw new SaldoInsuficienteException(
                "Saldo insuficiente. Saldo: R$ " + String.format("%.2f", saldo) +
                (taxa > 0 ? " | Taxa adicional: R$ " + taxa : ""));

        saldo -= totalDebitar;
        saquesMesAtual++;
        registrarTransacao("SAQUE" + (taxa > 0 ? " (+taxa R$" + taxa + ")" : ""), -totalDebitar);
    }

    /** Conta Salário não possui rendimento. */
    @Override
    public void aplicarRendimento() { /* sem rendimento */ }

    public void resetarSaquesMensais() {
        this.saquesMesAtual = 0;
    }

    public int getSaquesMesAtual() { return saquesMesAtual; }

    @Override
    public String toString() {
        return super.toString() + " | Saques no mês: " + saquesMesAtual;
    }
}
