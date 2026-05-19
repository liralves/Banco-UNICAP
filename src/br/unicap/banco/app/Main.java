package br.unicap.banco.app;

import br.unicap.banco.servico.Banco;

/**
 * Ponto de entrada do sistema Banco UNICAP.
 * Inicializa o banco com dados de demonstração e abre o menu.
 */
public class Main {

    public static void main(String[] args) {
        Banco banco = new Banco("Banco UNICAP");

        // --- Dados iniciais para demonstração ---
        try {
            banco.cadastrarFuncionario(
                "Ana Souza", "11122233344", "Professora", "ana@unicap.br", "ana", "1234"
            );
            banco.cadastrarFuncionario(
                "Carlos Lima", "55566677788", "Técnico Administrativo", "carlos@unicap.br", "carlos", "5678"
            );

            // Abre uma conta corrente para Ana já com saldo inicial (via depósito)
            var ana = banco.buscarFuncionarioPorCpf("11122233344");
            var contaAna = banco.abrirContaCorrente(ana, 1000.0);
            contaAna.depositar(3500.0);

            // Abre conta poupança para Carlos
            var carlos = banco.buscarFuncionarioPorCpf("55566677788");
            var contaCarlos = banco.abrirContaPoupanca(carlos);
            contaCarlos.depositar(1200.0);

        } catch (Exception e) {
            System.err.println("Erro ao inicializar dados de demonstração: " + e.getMessage());
        }

        // Inicializa o menu interativo
        Menu menu = new Menu(banco);
        menu.iniciar();
    }
}
