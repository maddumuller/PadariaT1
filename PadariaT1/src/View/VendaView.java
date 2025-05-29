package View;

import Controller.VendaController;
import Dao.ConexaoBD;
import Dao.VendaDao;
import Model.Cliente;
import Model.Produto;
import Model.ProdutoVenda;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class VendaView extends JFrame {
    private JComboBox<Cliente> clienteComboBox;
    private JList<String> produtosList;
    private DefaultListModel<String> produtosModel;
    private JButton adicionarProdutoButton, finalizarVendaButton, removerProdutoButton;
    private JLabel pontosLabel, subtotalLabel;
    private VendaController vendaController;

    private int totalPontos = 0;
    private double subtotal = 0.0;

    private List<Double> precos = new ArrayList<>();
    private List<Integer> pontos = new ArrayList<>();

    public VendaView(VendaController vendaController) {
        this.vendaController = vendaController;

        setTitle("Registrar Venda");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel do cliente
        JPanel clientePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clientePanel.add(new JLabel("Cliente:"));
        clienteComboBox = new JComboBox<>();
        clientePanel.add(clienteComboBox);
        panel.add(clientePanel, BorderLayout.NORTH);

        // Lista de produtos
        produtosModel = new DefaultListModel<>();
        produtosList = new JList<>(produtosModel);
        produtosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(produtosList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Informações
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        subtotalLabel = new JLabel("Subtotal: R$ 0.00");
        pontosLabel = new JLabel("Pontos ganhos: 0");
        infoPanel.add(subtotalLabel);
        infoPanel.add(pontosLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout());
        adicionarProdutoButton = new JButton("Adicionar Produto");
        removerProdutoButton = new JButton("Remover Produto");
        finalizarVendaButton = new JButton("Finalizar Venda");
        botoesPanel.add(adicionarProdutoButton);
        botoesPanel.add(removerProdutoButton);
        botoesPanel.add(finalizarVendaButton);

        add(panel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);
//
//        carregarClientes();

        // Ação adicionar produto
        adicionarProdutoButton.addActionListener(e -> {
            String nomeProduto = JOptionPane.showInputDialog(this, "Nome do Produto:");
            String precoStr = JOptionPane.showInputDialog(this, "Preço:");
            String pontosStr = JOptionPane.showInputDialog(this, "Pontos:");

            try {
                double preco = Double.parseDouble(precoStr);
                int pts = Integer.parseInt(pontosStr);
                addProduto(nomeProduto, preco, pts);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entrada inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação remover produto
        removerProdutoButton.addActionListener(e -> {
            int index = produtosList.getSelectedIndex();
            if (index != -1) {
                removerProduto(index);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

//        // Ação finalizar venda
//        finalizarVendaButton.addActionListener(e -> finalizarVenda());
    }

//    private void carregarClientes() {
//        List<Cliente> clientes = vendaController.listarClientes();
//        for (Cliente c : clientes) {
//            clienteComboBox.addItem(c);
//        }
//    }

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

    public void limparCampos() {
        produtosModel.clear();
        precos.clear();
        pontos.clear();
        subtotal = 0.0;
        totalPontos = 0;
        atualizarInfo();
    }

//    private void finalizarVenda() {
//        Cliente cliente = (Cliente) clienteComboBox.getSelectedItem();
//
//        if (cliente == null) {
//            JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        if (produtosModel.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto.", "Erro", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        List<ProdutoVenda> produtosVenda = new ArrayList<>();
//        for (int i = 0; i < produtosModel.size(); i++) {
//            String descricao = produtosModel.getElementAt(i);
//            String nomeProduto = descricao.split(" - ")[0];
//
//            Produto produto = vendaController.buscarProdutoPorNome(nomeProduto);
//            if (produto == null) {
//                JOptionPane.showMessageDialog(this, "Produto '" + nomeProduto + "' não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            ProdutoVenda pv = new ProdutoVenda(produto, 1); // ajuste se precisar
//            produtosVenda.add(pv);
//        }
//
//        int idVenda = vendaController.cadastrarVenda(cliente, produtosVenda);
//
//        if (idVenda > 0) {
//            JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!\nID da Venda: " + idVenda, "Venda Concluída", JOptionPane.INFORMATION_MESSAGE);
//            limparCampos();
//        } else {
//            JOptionPane.showMessageDialog(this, "Erro ao registrar a venda!", "Erro", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    public static void main(String[] args) {
        try {
            Connection connection = ConexaoBD.conectar();
            VendaController controller = new VendaController(connection);
            VendaView view = new VendaView(controller);
            view.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

