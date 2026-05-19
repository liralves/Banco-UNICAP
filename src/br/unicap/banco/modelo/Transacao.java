package br.unicap.banco.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma transação registrada no extrato de uma conta.
 */
public class Transacao {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String descricao;
    private double valor;
    private double saldoAposTransacao;
    private LocalDateTime dataHora;

    public Transacao(String descricao, double valor, double saldoAposTransacao) {
        this.descricao = descricao;
        this.valor = valor;
        this.saldoAposTransacao = saldoAposTransacao;
        this.dataHora = LocalDateTime.now();
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getValor() { return valor; }
    public double getSaldoAposTransacao() { return saldoAposTransacao; }
    public LocalDateTime getDataHora() { return dataHora; }

    @Override
    public String toString() {
        String sinal = valor >= 0 ? "+" : "";
        return String.format("%-28s | %s%-10.2f | Saldo: R$ %10.2f | %s",
                descricao, sinal, valor, saldoAposTransacao, dataHora.format(FORMATTER));
    }
}
