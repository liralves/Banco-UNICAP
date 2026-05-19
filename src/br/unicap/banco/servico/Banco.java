    package br.unicap.banco.servico;

    import br.unicap.banco.excecoes.*;
    import br.unicap.banco.modelo.*;

    import java.util.*;

    /**
     * Classe central do sistema bancário.
     * Gerencia funcionários, usuários e contas.
     */
    public class Banco {

        private String nome;
        private Map<String, Funcionario> funcionariosPorCpf;   // CPF → Funcionario
        private Map<String, Usuario> usuariosPorLogin;          // login → Usuario
        private Map<Integer, Conta> contasPorNumero;            // numero → Conta
        private Map<Integer, List<Conta>> contasPorFuncionario; // id funcionario → lista de contas

        public Banco(String nome) {
            this.nome = nome;
            this.funcionariosPorCpf = new HashMap<>();
            this.usuariosPorLogin = new HashMap<>();
            this.contasPorNumero = new HashMap<>();
            this.contasPorFuncionario = new HashMap<>();
        }

        // ==============================
        // FUNCIONÁRIOS
        // ==============================

        public Funcionario cadastrarFuncionario(String nome, String cpf, String cargo,
                                                String email, String login, String senha) throws Exception {
            if (funcionariosPorCpf.containsKey(cpf))
                throw new Exception("Já existe um funcionário com o CPF: " + cpf);
            if (usuariosPorLogin.containsKey(login))
                throw new Exception("Login '" + login + "' já está em uso.");

            Funcionario f = new Funcionario(nome, cpf, cargo, email, senha);
            Usuario u = new Usuario(login, senha, f);

            funcionariosPorCpf.put(cpf, f);
            usuariosPorLogin.put(login, u);
            contasPorFuncionario.put(f.getId(), new ArrayList<>());
            return f;
        }

        public Funcionario buscarFuncionarioPorCpf(String cpf) throws FuncionarioNaoEncontradoException {
            Funcionario f = funcionariosPorCpf.get(cpf);
            if (f == null) throw new FuncionarioNaoEncontradoException("Funcionário com CPF " + cpf + " não encontrado.");
            return f;
        }

        public Collection<Funcionario> listarFuncionarios() {
            return funcionariosPorCpf.values();
        }

        // ==============================
        // AUTENTICAÇÃO
        // ==============================

        public Usuario autenticar(String login, String senha) throws AutenticacaoException {
            Usuario u = usuariosPorLogin.get(login);
            if (u == null || !u.autenticar(senha))
                throw new AutenticacaoException("Login ou senha incorretos.");
            return u;
        }

        // ==============================
        // CONTAS
        // ==============================

        public ContaCorrente abrirContaCorrente(Funcionario titular) {
            ContaCorrente c = new ContaCorrente(titular);
            registrarConta(titular, c);
            return c;
        }

        public ContaCorrente abrirContaCorrente(Funcionario titular, double limite) {
            ContaCorrente c = new ContaCorrente(titular, limite);
            registrarConta(titular, c);
            return c;
        }

        public ContaPoupanca abrirContaPoupanca(Funcionario titular) {
            ContaPoupanca c = new ContaPoupanca(titular);
            registrarConta(titular, c);
            return c;
        }

        public ContaSalario abrirContaSalario(Funcionario titular) {
            ContaSalario c = new ContaSalario(titular);
            registrarConta(titular, c);
            return c;
        }

        private void registrarConta(Funcionario titular, Conta conta) {
            contasPorNumero.put(conta.getNumero(), conta);
            contasPorFuncionario.get(titular.getId()).add(conta);
        }

        public Conta buscarContaPorNumero(int numero) throws Exception {
            Conta c = contasPorNumero.get(numero);
            if (c == null) throw new Exception("Conta #" + numero + " não encontrada.");
            return c;
        }

        public List<Conta> listarContasPorFuncionario(Funcionario f) {
            return contasPorFuncionario.getOrDefault(f.getId(), new ArrayList<>());
        }

        public Collection<Conta> listarTodasContas() {
            return contasPorNumero.values();
        }

        public String getNome() { return nome; }
    }
