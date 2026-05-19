package br.unicap.banco.modelo;

/**
 * Conta Corrente com limite de cheque especial.
 * Herda de Conta e implementa comportamento específico.
 */
public class ContaCorrente extends Conta {

    private double limiteEspecial;

    public ContaCorrente(Funcionario titular) {
        super(titular);
        this.limiteEspecial = 500.0; // limite padrão
    }

    public ContaCorrente(Funcionario titular, double limiteEspecial) {
        super(titular);
        this.limiteEspecial = limiteEspecial;
    }

    @Override
    public String getTipoConta() {
        return "CORRENTE";
    }

    @Override
    public double getLimiteEspecial() {
        return limiteEspecial;
    }

    /**
     * Conta Corrente não possui rendimento mensal automático.
     * Cobra taxa de manutenção de R$ 12,00 se saldo positivo.
     */
    @Override
    public void aplicarRendimento() {
        double taxa = 12.0;
        if (saldo >= taxa) {
            saldo -= taxa;
            registrarTransacao("TAXA DE MANUTENÇÃO", -taxa);
        }
    }

    public void setLimiteEspecial(double limite) {
        this.limiteEspecial = limite;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Limite Especial: R$ %.2f", limiteEspecial);
    }
}
