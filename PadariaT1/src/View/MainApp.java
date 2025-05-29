package View;

import Controller.ClienteController;
import Controller.ProdutoController;
import Dao.ConexaoBD;
import Dao.ProdutoDao;
import java.sql.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp {

    private ClienteView clienteView;
    private MenuPrincipal menuPrincipal;
    private ProdutoView produtoView;
    private VendaView vendaView;
    private TrocaPontosView trocaPontosView;
    private List<Produto> produtos;
    private List<Cliente> clientes;
    private Connection conexao;
    private ProdutoController produtoController;

    public MainApp() {
        // Inicializa listas em memória para produtos e clientes
//        produtos = new ArrayList<>();
//        clientes = new ArrayList<>();
//        // Adiciona alguns dados de exemplo
//        clientes.add(new Cliente("Cliente Exemplo 1", 100));
//        clientes.add(new Cliente("Cliente Exemplo 2", 50));
//        produtos.add(new Produto("Pão Francês", 5.00, "Pão", 100, true, 50));
//        produtos.add(new Produto("Bolo de Chocolate", 20.00, "Bolo", 10, true, 200));

//        try {
//            this.conexao = ConexaoBD.conectar();
//            ProdutoDao produtoDao = new ProdutoDao(conexao);
//            this.produtoController = new ProdutoController(produtoDao);
//            // inicialize seu ClienteController aqui, se necessário
//            // this.clienteController = new ClienteController(...);
//        } catch (RuntimeException e) {
//            JOptionPane.showMessageDialog(this, "Erro na conexão com o banco: " + e.getMessage());
//            System.exit(1);
//        }



        // Inicializa as telas
        clienteView = new ClienteView();
        menuPrincipal = new MenuPrincipal();
        produtoView = new ProdutoView();
        vendaView = new VendaView();
        trocaPontosView = new TrocaPontosView();

        // Exibe o menu principal
        menuPrincipal.setVisible(true);
    }

    // Métodos para abrir as telas
    public void abrirProdutoView() {
        produtoView.setVisible(true);
        menuPrincipal.setVisible(false);
    }

    public void abrirVendaView() {
        vendaView.carregarClientes();
        vendaView.setVisible(true);
        menuPrincipal.setVisible(false);
    }

    public void abrirTrocaPontosView() {
        trocaPontosView.setVisible(true);
        menuPrincipal.setVisible(false);
    }

    public void abrirClienteView() {
        clienteView.setVisible(true);
        menuPrincipal.setVisible(false);
    }

    // Método para voltar ao menu principal
    public void voltarParaMenuPrincipal(JFrame telaAtual) {
        telaAtual.dispose();
        menuPrincipal.setVisible(true);
    }

    // Métodos para gerenciar produtos e clientes
    public void cadastrarProduto(String nome, double preco, String tipo, int quantidade, boolean resgatavel, int custoPontos) {
        produtos.add(new Produto(nome, preco, tipo, quantidade, resgatavel, custoPontos));
        if (resgatavel) {
            trocaPontosView.addProdutoResgatavel(nome);
        }
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    // Método para adicionar produto em VendaView
    public void abrirDialogoAdicionarProduto(VendaView vendaView) {
        JComboBox<String> produtoCombo = new JComboBox<>();
        for (Produto p : produtos) {
            produtoCombo.addItem(p.getNome() + " - R$ " + String.format("%.2f", p.getPreco()));
        }

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Selecione o Produto:"));
        panel.add(produtoCombo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && produtoCombo.getSelectedItem() != null) {
            int index = produtoCombo.getSelectedIndex();
            Produto p = produtos.get(index);
            if (p.getQuantidade() > 0) {
                vendaView.addProduto(p.getNome(), p.getPreco(), (int)(p.getPreco() * 10)); // 10 pontos por real
                p.setQuantidade(p.getQuantidade() - 1);
            } else {
                JOptionPane.showMessageDialog(null, "Produto sem estoque!");
            }
        }
    }

    // Método para realizar troca de pontos
    public void realizarTroca(TrocaPontosView trocaView) {
        String clienteNome = trocaView.getClienteSelecionado();
        String produtoNome = trocaView.getProdutoSelecionado();
        if (clienteNome != null && produtoNome != null) {
            Cliente cliente = clientes.stream().filter(c -> c.getNome().equals(clienteNome)).findFirst().orElse(null);
            Produto produto = produtos.stream().filter(p -> p.getNome().equals(produtoNome)).findFirst().orElse(null);
            if (cliente != null && produto != null && produto.isResgatavel()) {
                if (cliente.getPontos() >= produto.getCustoPontos()) {
                    cliente.setPontos(cliente.getPontos() - produto.getCustoPontos());
                    produto.setQuantidade(produto.getQuantidade() - 1);
                    trocaView.setPontosDisponiveis(cliente.getPontos());
                    JOptionPane.showMessageDialog(null, "Troca realizada: " + produtoNome + " para " + clienteNome);
                    if (produto.getQuantidade() == 0) {
                        trocaView.removerProdutoResgatavel(produtoNome);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Pontos insuficientes!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cliente ou produto inválido.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione cliente e produto.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }

    // Classe para representar produtos
    private class Produto {
        private String nome;
        private double preco;
        private String tipo;
        private int quantidade;
        private boolean resgatavel;
        private int custoPontos;

        public Produto(String nome, double preco, String tipo, int quantidade, boolean resgatavel, int custoPontos) {
            this.nome = nome;
            this.preco = preco;
            this.tipo = tipo;
            this.quantidade = quantidade;
            this.resgatavel = resgatavel;
            this.custoPontos = custoPontos;
        }

        public String getNome() { return nome; }
        public double getPreco() { return preco; }
        public String getTipo() { return tipo; }
        public int getQuantidade() { return quantidade; }
        public boolean isResgatavel() { return resgatavel; }
        public int getCustoPontos() { return custoPontos; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    }


    //Classe para representar clientes
    private class Cliente {
        private String nome;
        private int pontos;

        public Cliente(String nome, int pontos) {
            this.nome = nome;
            this.pontos = pontos;
        }

        public String getNome() { return nome; }
        public int getPontos() { return pontos; }
        public void setPontos(int pontos) { this.pontos = pontos; }
    }

    private class MenuPrincipal extends JFrame {
        public MenuPrincipal() {
            setTitle("Menu Principal - PadariaT1");
            setSize(400, 350);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JButton produtoButton = new JButton("Cadastro de Produto");
            JButton clienteButton = new JButton("Cadastro de Cliente");
            JButton vendaButton = new JButton("Cadastro de Venda");
            JButton trocaButton = new JButton("Troca de Pontos");
            JButton sairButton = new JButton("Sair");

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(produtoButton, gbc);
            gbc.gridy = 1;
            panel.add(clienteButton, gbc);
            gbc.gridy = 2;
            panel.add(vendaButton, gbc);
            gbc.gridy = 3;
            panel.add(trocaButton, gbc);
            gbc.gridy = 4;
            panel.add(sairButton, gbc);

            add(panel);

            produtoButton.addActionListener(e -> abrirProdutoView());
            clienteButton.addActionListener(e -> abrirClienteView());
            vendaButton.addActionListener(e -> abrirVendaView());
            trocaButton.addActionListener(e -> abrirTrocaPontosView());
            sairButton.addActionListener(e -> System.exit(0));
        }
    }

    private class VendaView extends JFrame {
        private JComboBox<String> clienteComboBox;
        private JList<String> produtosList;
        private DefaultListModel<String> produtosModel;
        private JButton adicionarProdutoButton, finalizarVendaButton, removerProdutoButton;
        private JLabel pontosLabel, subtotalLabel;
        private List<Double> precos = new ArrayList<>();
        private List<Integer> pontos = new ArrayList<>();
        private double subtotal = 0.0;
        private int totalPontos = 0;

        public VendaView() {
            setTitle("Registrar Venda");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(600, 450);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            JPanel clientePanel = new JPanel(new BorderLayout());
            clientePanel.add(new JLabel("Cliente:"), BorderLayout.WEST);

            // Inicializa o ComboBox sem dados
            clienteComboBox = new JComboBox<>();
            clientePanel.add(clienteComboBox, BorderLayout.CENTER);
            panel.add(clientePanel, BorderLayout.NORTH);

            produtosModel = new DefaultListModel<>();
            produtosList = new JList<>(produtosModel);
            produtosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(produtosList);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            subtotalLabel = new JLabel("Subtotal: R$ 0.00");
            pontosLabel = new JLabel("Pontos ganhos: 0");
            infoPanel.add(subtotalLabel);
            infoPanel.add(pontosLabel);
            panel.add(infoPanel, BorderLayout.SOUTH);

            JPanel botoesPanel = new JPanel(new FlowLayout());
            adicionarProdutoButton = new JButton("Adicionar Produto");
            removerProdutoButton = new JButton("Remover Produto");
            finalizarVendaButton = new JButton("Finalizar Venda");
            JButton voltarButton = new JButton("Voltar");
            botoesPanel.add(adicionarProdutoButton);
            botoesPanel.add(removerProdutoButton);
            botoesPanel.add(finalizarVendaButton);
            botoesPanel.add(voltarButton);
            add(panel, BorderLayout.CENTER);
            add(botoesPanel, BorderLayout.SOUTH);

            adicionarProdutoButton.addActionListener(e -> abrirDialogoAdicionarProduto(this));
            removerProdutoButton.addActionListener(e -> {
                int index = produtosList.getSelectedIndex();
                if (index != -1) {
                    removerProduto(index);
                } else {
                    JOptionPane.showMessageDialog(this, "Selecione um produto para remover.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
            finalizarVendaButton.addActionListener(e -> finalizarVenda());
            voltarButton.addActionListener(e -> voltarParaMenuPrincipal(this));
        }

        // Novo método para carregar clientes
        public void carregarClientes() {
            clienteComboBox.removeAllItems();
            for (Cliente c : clientes) {
                clienteComboBox.addItem(c.getNome());
            }
        }

        public void addProduto(String nome, double preco, int pts) {
            produtosModel.addElement(nome + " - R$ " + String.format("%.2f", preco) + " (+" + pts + " pts)");
            precos.add(preco);
            pontos.add(pts);
            subtotal += preco;
            totalPontos += pts;
            atualizarInfo();
        }

        public void removerProduto(int index) {
            subtotal -= precos.get(index);
            totalPontos -= pontos.get(index);
            precos.remove(index);
            pontos.remove(index);
            produtosModel.remove(index);
            atualizarInfo();
        }

        private void atualizarInfo() {
            subtotalLabel.setText("Subtotal: R$ " + String.format("%.2f", subtotal));
            pontosLabel.setText("Pontos ganhos: " + totalPontos);
        }

        private void finalizarVenda() {
            String clienteNome = (String) clienteComboBox.getSelectedItem();
            if (clienteNome == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente cliente = clientes.stream()
                    .filter(c -> c.getNome().equals(clienteNome))
                    .findFirst()
                    .orElse(null);

            if (cliente != null) {
                cliente.setPontos(cliente.getPontos() + totalPontos);
                JOptionPane.showMessageDialog(
                        this,
                        "Venda finalizada para: " + clienteNome +
                                "\nSubtotal: R$ " + String.format("%.2f", subtotal) +
                                "\nPontos ganhos: " + totalPontos,
                        "Venda Concluída",
                        JOptionPane.INFORMATION_MESSAGE
                );
                limparCampos();
                voltarParaMenuPrincipal(this);
            }
        }

        public void limparCampos() {
            clienteComboBox.setSelectedIndex(-1);
            produtosModel.clear();
            precos.clear();
            pontos.clear();
            subtotal = 0.0;
            totalPontos = 0;
            atualizarInfo();
        }
    }

    private class ClienteView extends JFrame {
        private JTextField cpfField;
        private JTextField nomeField;
        private JTextField telefoneField;
        private JButton cadastrarButton;

        public ClienteView() {
            setTitle("Cadastro de Cliente");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Campo CPF
            panel.add(new JLabel("CPF (somente números):"));
            cpfField = new JTextField();
            panel.add(cpfField);

            // Campo Nome
            panel.add(new JLabel("Nome:"));
            nomeField = new JTextField();
            panel.add(nomeField);

            // Campo Telefone
            panel.add(new JLabel("Telefone (DDD + número):"));
            telefoneField = new JTextField();
            panel.add(telefoneField);

            // Botões
            cadastrarButton = new JButton("Cadastrar Cliente");
            JButton voltarButton = new JButton("Voltar");
            panel.add(cadastrarButton);
            panel.add(voltarButton);

            add(panel);

            // Action Listeners
            cadastrarButton.addActionListener(e -> {
                try {
                    // Validação do CPF
                    String cpf = cpfField.getText().trim().replaceAll("[^0-9]", "");
                    if (cpf.length() != 11) {
                        throw new IllegalArgumentException("CPF deve conter 11 dígitos numéricos.");
                    }

                    // Validação do nome
                    String nome = nomeField.getText().trim();
                    if (nome.isEmpty()) {
                        throw new IllegalArgumentException("Nome é obrigatório.");
                    }

                    // Validação do telefone
                    String telefone = telefoneField.getText().trim().replaceAll("[^0-9]", "");
                    if (telefone.length() != 11) {
                        throw new IllegalArgumentException("Telefone deve conter 11 dígitos (DDD + 9 + número).");
                    }

                    // Verifica se já existe um cliente com o mesmo CPF
                    boolean clienteExistente = clientes.stream()
                            .anyMatch(c -> c.getNome().equalsIgnoreCase(nome));

                    if (clienteExistente) {
                        throw new IllegalArgumentException("Já existe um cliente cadastrado com este nome.");
                    }

                    // Cadastra o novo cliente (pontos começam em 0)
                    Cliente novoCliente = new Cliente(nome, 0);
                    clientes.add(novoCliente);

                    // Adiciona o cliente na tela de troca de pontos
                    trocaPontosView.addCliente(nome);

                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");

                    // Limpa os campos
                    limparCampos();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erro ao cadastrar: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            voltarButton.addActionListener(e -> voltarParaMenuPrincipal(this));
        }

        private void limparCampos() {
            cpfField.setText("");
            nomeField.setText("");
            telefoneField.setText("");
        }

        private String formatarTelefone(String telefone) {
            return String.format("(%s) %s%s-%s",
                    telefone.substring(0, 2),
                    telefone.substring(2, 3),
                    telefone.substring(3, 7),
                    telefone.substring(7));
        }

        private String formatarCPF(String cpf) {
            return String.format("%s.%s.%s-%s",
                    cpf.substring(0, 3),
                    cpf.substring(3, 6),
                    cpf.substring(6, 9),
                    cpf.substring(9));
        }
    }

    private class TrocaPontosView extends JFrame {
        private JComboBox<String> clienteComboBox;
        private JComboBox<String> produtoComboBox;
        private JButton trocarButton;
        private JLabel pontosDisponiveisLabel;

        public TrocaPontosView() {
            setTitle("Troca de Pontos");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 250);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
            clienteComboBox = new JComboBox<>();
            produtoComboBox = new JComboBox<>();
            trocarButton = new JButton("Trocar");
            pontosDisponiveisLabel = new JLabel("Pontos disponíveis: 0");
            JButton voltarButton = new JButton("Voltar");

            panel.add(new JLabel("Cliente:"));
            panel.add(clienteComboBox);
            panel.add(pontosDisponiveisLabel);
            panel.add(new JLabel("Produto Resgatável:"));
            panel.add(produtoComboBox);
            panel.add(voltarButton);

            add(panel, BorderLayout.CENTER);
            add(trocarButton, BorderLayout.SOUTH);

            // Popula os combos
            for (Cliente c : clientes) {
                clienteComboBox.addItem(c.getNome());
            }
            for (Produto p : produtos) {
                if (p.isResgatavel()) {
                    produtoComboBox.addItem(p.getNome());
                }
            }

            clienteComboBox.addActionListener(e -> {
                String clienteNome = (String) clienteComboBox.getSelectedItem();
                if (clienteNome != null) {
                    Cliente cliente = clientes.stream().filter(c -> c.getNome().equals(clienteNome)).findFirst().orElse(null);
                    if (cliente != null) {
                        setPontosDisponiveis(cliente.getPontos());
                    }
                }
            });

            trocarButton.addActionListener(e -> realizarTroca(this));
            voltarButton.addActionListener(e -> voltarParaMenuPrincipal(this));

            if (clienteComboBox.getItemCount() > 0) {
                clienteComboBox.setSelectedIndex(0);
            }
        }

        public String getClienteSelecionado() {
            return (String) clienteComboBox.getSelectedItem();
        }

        public String getProdutoSelecionado() {
            return (String) produtoComboBox.getSelectedItem();
        }

        public void setPontosDisponiveis(int pontos) {
            pontosDisponiveisLabel.setText("Pontos disponíveis: " + pontos);
        }

        public void addCliente(String cliente) {
            clienteComboBox.addItem(cliente);
        }

        public void addProdutoResgatavel(String produto) {
            produtoComboBox.addItem(produto);
        }

        public void removerProdutoResgatavel(String produto) {
            produtoComboBox.removeItem(produto);
        }
    }

    private class ProdutoView extends JFrame {
        private JTextField nomeField;
        private JTextField precoField;
        private JTextField tipoField;
        private JTextField quantidadeField;
        private JCheckBox resgatavelCheckBox;
        private JTextField custoPontosField;
        private JButton cadastrarButton;

        public ProdutoView() {
            setTitle("Cadastro de Produto");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 350);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            panel.add(new JLabel("Nome:"));
            nomeField = new JTextField();
            panel.add(nomeField);

            panel.add(new JLabel("Preço:"));
            precoField = new JTextField();
            panel.add(precoField);

            panel.add(new JLabel("Tipo:"));
            tipoField = new JTextField();
            panel.add(tipoField);

            panel.add(new JLabel("Quantidade em estoque:"));
            quantidadeField = new JTextField();
            panel.add(quantidadeField);

            panel.add(new JLabel("Resgatável com pontos:"));
            resgatavelCheckBox = new JCheckBox();
            panel.add(resgatavelCheckBox);

            panel.add(new JLabel("Custo em pontos:"));
            custoPontosField = new JTextField();
            panel.add(custoPontosField);

            cadastrarButton = new JButton("Cadastrar Produto");
            panel.add(cadastrarButton);

            JButton voltarButton = new JButton("Voltar");
            panel.add(voltarButton);

            add(panel);

            cadastrarButton.addActionListener(e -> {
                try {
                    String nome = nomeField.getText().trim();
                    if (nome.isEmpty()) throw new IllegalArgumentException("Nome é obrigatório.");
                    double preco = Double.parseDouble(precoField.getText());
                    if (preco <= 0) throw new IllegalArgumentException("Preço deve ser maior que zero.");
                    String tipo = tipoField.getText().trim();
                    if (tipo.isEmpty()) throw new IllegalArgumentException("Tipo é obrigatório.");
                    int quantidade = Integer.parseInt(quantidadeField.getText());
                    if (quantidade < 0) throw new IllegalArgumentException("Quantidade não pode ser negativa.");
                    boolean resgatavel = resgatavelCheckBox.isSelected();
                    int custoPontos = resgatavel ? Integer.parseInt(custoPontosField.getText()) : 0;
                    if (resgatavel && custoPontos <= 0) throw new IllegalArgumentException("Custo em pontos deve ser maior que zero.");

                    cadastrarProduto(nome, preco, tipo, quantidade, resgatavel, custoPontos);
                    JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                    nomeField.setText("");
                    precoField.setText("");
                    tipoField.setText("");
                    quantidadeField.setText("");
                    resgatavelCheckBox.setSelected(false);
                    custoPontosField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
                }
            });

            voltarButton.addActionListener(e -> voltarParaMenuPrincipal(this));
        }
    }
}