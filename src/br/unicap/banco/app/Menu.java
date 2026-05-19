package br.unicap.banco.app;

import br.unicap.banco.excecoes.*;
import br.unicap.banco.modelo.*;
import br.unicap.banco.servico.Banco;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de terminal do Banco UNICAP.
 * Gerencia o fluxo de menus e interação com o usuário.
 */
public class Menu {

    private static final String LINHA = "═".repeat(60);
    private static final String SEP   = "─".repeat(60);

    private Banco banco;
    private Scanner scanner;
    private Usuario usuarioLogado;

    public Menu(Banco banco) {
        this.banco = banco;
        this.scanner = new Scanner(System.in);
    }

    // ==============================
    // ENTRADA PRINCIPAL
    // ==============================

    public void iniciar() {
        exibirBanner();
        boolean executando = true;
        while (executando) {
            exibirMenuInicial();
            int opcao = lerInt("Opção");
            switch (opcao) {
                case 1 -> fazerLogin();
                case 2 -> cadastrarFuncionario();
                case 0 -> { System.out.println("\n  Até logo! 👋\n"); executando = false; }
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    private void exibirBanner() {
        System.out.println("\n" + LINHA);
        System.out.println("       🏦  BANCO UNICAP — SISTEMA BANCÁRIO DIGITAL");
        System.out.println(LINHA);
    }

    private void exibirMenuInicial() {
        System.out.println("\n" + SEP);
        System.out.println("  [1] Login");
        System.out.println("  [2] Cadastrar novo funcionário");
        System.out.println("  [0] Sair");
        System.out.println(SEP);
    }

    // ==============================
    // AUTENTICAÇÃO
    // ==============================

    private void fazerLogin() {
        System.out.println("\n--- LOGIN ---");
        String login = lerTexto("Login");
        String senha = lerTexto("Senha");
        try {
            usuarioLogado = banco.autenticar(login, senha);
            System.out.println("  ✅ Bem-vindo, " + usuarioLogado.getFuncionario().getNome() + "!");
            menuLogado();
        } catch (AutenticacaoException e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    // ==============================
    // MENU PÓS-LOGIN
    // ==============================

    private void menuLogado() {
        boolean logado = true;
        while (logado) {
            Funcionario f = usuarioLogado.getFuncionario();
            System.out.println("\n" + LINHA);
            System.out.println("  Usuário: " + f.getNome() + " | " + f.getCargo());
            System.out.println(LINHA);
            System.out.println("  [1]  Abrir Conta Corrente");
            System.out.println("  [2]  Abrir Conta Poupança");
            System.out.println("  [3]  Abrir Conta Salário");
            System.out.println("  [4]  Consultar Saldo");
            System.out.println("  [5]  Depositar");
            System.out.println("  [6]  Sacar");
            System.out.println("  [7]  Transferir");
            System.out.println("  [8]  Exibir Extrato");
            System.out.println("  [9]  Encerrar Conta");
            System.out.println("  [10] Minhas Contas");
            System.out.println("  [11] Listar todos os funcionários");
            System.out.println("  [12] Listar todas as contas");
            System.out.println("  [0]  Logout");
            System.out.println(SEP);

            int opcao = lerInt("Opção");
            switch (opcao) {
                case 1  -> abrirConta("corrente");
                case 2  -> abrirConta("poupanca");
                case 3  -> abrirConta("salario");
                case 4  -> consultarSaldo();
                case 5  -> depositar();
                case 6  -> sacar();
                case 7  -> transferir();
                case 8  -> exibirExtrato();
                case 9  -> encerrarConta();
                case 10 -> listarMinhasContas();
                case 11 -> listarFuncionarios();
                case 12 -> listarTodasContas();
                case 0  -> { System.out.println("  👋 Logout realizado."); usuarioLogado = null; logado = false; }
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    // ==============================
    // OPERAÇÕES
    // ==============================

    private void cadastrarFuncionario() {
        System.out.println("\n--- CADASTRO DE FUNCIONÁRIO ---");
        String nome   = lerTexto("Nome completo");
        String cpf    = lerTexto("CPF (somente números)");
        String cargo  = lerTexto("Cargo");
        String email  = lerTexto("E-mail");
        String login  = lerTexto("Login desejado");
        String senha  = lerTexto("Senha");

        try {
            Funcionario f = banco.cadastrarFuncionario(nome, cpf, cargo, email, login, senha);
            System.out.println("  ✅ Funcionário cadastrado com sucesso! ID: " + f.getId());
        } catch (Exception e) {
            System.out.println("  ❌ Erro: " + e.getMessage());
        }
    }

    private void abrirConta(String tipo) {
        Funcionario f = usuarioLogado.getFuncionario();
        Conta nova = null;
        switch (tipo) {
            case "corrente" -> {
                System.out.print("  Deseja definir um limite especial? (s/n): ");
                String resp = scanner.nextLine().trim().toLowerCase();
                if (resp.equals("s")) {
                    double limite = lerDouble("Limite especial (R$)");
                    nova = banco.abrirContaCorrente(f, limite);
                } else {
                    nova = banco.abrirContaCorrente(f);
                }
            }
            case "poupanca" -> nova = banco.abrirContaPoupanca(f);
            case "salario"  -> nova = banco.abrirContaSalario(f);
        }
        if (nova != null)
            System.out.println("  ✅ Conta aberta! Número: #" + nova.getNumero() + " | Tipo: " + nova.getTipoConta());
    }

    private void consultarSaldo() {
        try {
            Conta c = selecionarConta();
            System.out.printf("  💰 Saldo da conta #%d: R$ %.2f%n", c.getNumero(), c.getSaldo());
            if (c instanceof ContaCorrente cc)
                System.out.printf("     Limite especial disponível: R$ %.2f%n", cc.getLimiteEspecial());
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    private void depositar() {
        try {
            Conta c = selecionarConta();
            double valor = lerDouble("Valor do depósito (R$)");
            c.depositar(valor);
            System.out.printf("  ✅ Depósito de R$ %.2f realizado. Novo saldo: R$ %.2f%n", valor, c.getSaldo());
        } catch (ContaInativaException e) {
            System.out.println("  ❌ Conta inativa: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    private void sacar() {
        try {
            Conta c = selecionarConta();
            double valor = lerDouble("Valor do saque (R$)");
            c.sacar(valor);
            System.out.printf("  ✅ Saque de R$ %.2f realizado. Novo saldo: R$ %.2f%n", valor, c.getSaldo());
        } catch (SaldoInsuficienteException | ContaInativaException e) {
            System.out.println("  ❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    private void transferir() {
        try {
            System.out.println("  -- Conta de ORIGEM --");
            Conta origem = selecionarConta();
            int numDestino = lerInt("Número da conta de DESTINO");
            Conta destino = banco.buscarContaPorNumero(numDestino);
            double valor = lerDouble("Valor da transferência (R$)");
            origem.transferir(destino, valor);
            System.out.printf("  ✅ Transferência de R$ %.2f realizada para conta #%d.%n", valor, destino.getNumero());
        } catch (SaldoInsuficienteException | ContaInativaException e) {
            System.out.println("  ❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    private void exibirExtrato() {
        try {
            Conta c = selecionarConta();
            List<Transacao> extrato = c.getExtrato();
            System.out.println("\n" + LINHA);
            System.out.printf("  EXTRATO — Conta #%d (%s)%n", c.getNumero(), c.getTipoConta());
            System.out.println("  Titular: " + c.getTitular().getNome());
            System.out.println(SEP);
            if (extrato.isEmpty()) {
                System.out.println("  Nenhuma transação registrada.");
            } else {
                extrato.forEach(t -> System.out.println("  " + t));
            }
            System.out.println(SEP);
            System.out.printf("  Saldo atual: R$ %.2f%n", c.getSaldo());
            System.out.println(LINHA);
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    private void encerrarConta() {
        try {
            Conta c = selecionarConta();
            System.out.print("  ⚠ Confirma o encerramento da conta #" + c.getNumero() + "? (s/n): ");
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("s")) {
                c.encerrar();
                System.out.println("  ✅ Conta #" + c.getNumero() + " encerrada com sucesso.");
            } else {
                System.out.println("  Operação cancelada.");
            }
        } catch (ContaInativaException e) {
            System.out.println("  ❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getMessage());
        }
    }

    private void listarMinhasContas() {
        Funcionario f = usuarioLogado.getFuncionario();
        List<Conta> contas = banco.listarContasPorFuncionario(f);
        System.out.println("\n  --- SUAS CONTAS ---");
        if (contas.isEmpty()) {
            System.out.println("  Nenhuma conta encontrada.");
        } else {
            contas.forEach(c -> System.out.println("  " + c));
        }
    }

    private void listarFuncionarios() {
        System.out.println("\n  --- FUNCIONÁRIOS CADASTRADOS ---");
        banco.listarFuncionarios().forEach(f -> System.out.println("  " + f));
    }

    private void listarTodasContas() {
        System.out.println("\n  --- TODAS AS CONTAS ---");
        banco.listarTodasContas().forEach(c -> System.out.println("  " + c));
    }

    // ==============================
    // HELPERS
    // ==============================

    private Conta selecionarConta() throws Exception {
        listarMinhasContas();
        int num = lerInt("Número da conta");
        Conta c = banco.buscarContaPorNumero(num);
        // Verifica se a conta pertence ao usuário logado
        List<Conta> minhas = banco.listarContasPorFuncionario(usuarioLogado.getFuncionario());
        if (minhas.stream().noneMatch(x -> x.getNumero() == num))
            throw new Exception("Você não tem permissão para operar nessa conta.");
        return c;
    }

    private int lerInt(String campo) {
        while (true) {
            System.out.print("  " + campo + ": ");
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Por favor, insira um número inteiro válido.");
            }
        }
    }

    private double lerDouble(String campo) {
        while (true) {
            System.out.print("  " + campo + ": ");
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Por favor, insira um valor numérico válido (ex: 150.00).");
            }
        }
    }

    private String lerTexto(String campo) {
        System.out.print("  " + campo + ": ");
        return scanner.nextLine().trim();
    }
}
