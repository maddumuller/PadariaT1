package View;

import Controller.VendaController;
import Model.ProdutoVenda;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VendaView extends JFrame {
    private JComboBox<String> clienteComboBox;
    private JList<String> produtosList;
    private DefaultListModel<String> produtosModel;
    private JButton adicionarProdutoButton, finalizarVendaButton, removerProdutoButton, voltarButton;
    private JLabel pontosLabel, subtotalLabel;
    private VendaController vendaController;

    private double subtotal = 0.0;
    private int totalPontos = 0;

    private List<ProdutoVenda> produtosVenda = new ArrayList<>();
    private List<Integer> pontos = new ArrayList<>();

    public VendaView(VendaController controller) {
        this.vendaController = controller;
        setTitle("Registrar Venda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Cliente
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

        // Informações de subtotal e pontos
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        subtotalLabel = new JLabel("Subtotal: R$ 0.00", SwingConstants.CENTER);
        pontosLabel = new JLabel("Pontos ganhos: 0", SwingConstants.CENTER);
        Font infoFont = new Font("Arial", Font.BOLD, 16);
        subtotalLabel.setFont(infoFont);
        pontosLabel.setFont(infoFont);
        subtotalLabel.setBorder(BorderFactory.createTitledBorder("Subtotal"));
        pontosLabel.setBorder(BorderFactory.createTitledBorder("Pontos Ganhos"));
        infoPanel.add(subtotalLabel);
        infoPanel.add(pontosLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);

        // Botões
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        adicionarProdutoButton = new JButton("Adicionar Produto");
        removerProdutoButton = new JButton("Remover Produto");
        finalizarVendaButton = new JButton("Finalizar Venda");
        voltarButton = new JButton("Voltar");
        Dimension btnSize = new Dimension(140, 30);
        adicionarProdutoButton.setPreferredSize(btnSize);
        removerProdutoButton.setPreferredSize(btnSize);
        finalizarVendaButton.setPreferredSize(btnSize);
        voltarButton.setPreferredSize(btnSize);
        bottomPanel.add(adicionarProdutoButton);
        bottomPanel.add(removerProdutoButton);
        bottomPanel.add(finalizarVendaButton);
        bottomPanel.add(voltarButton);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        carregarClientes();

        // Ações
        adicionarProdutoButton.addActionListener(e -> {
            ProdutoVenda pv = vendaController.selecionarProdutoDialog(this);
            if (pv != null) {
                addProdutoVenda(pv);
            }
        });

        removerProdutoButton.addActionListener(e -> {
            int index = produtosList.getSelectedIndex();
            if (index != -1) {
                removerProduto(index);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        finalizarVendaButton.addActionListener(e -> finalizarVenda());

        voltarButton.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });
    }

    private void carregarClientes() {
        clienteComboBox.removeAllItems();
        for (String clienteNome : vendaController.getClientesNomes()) {
            clienteComboBox.addItem(clienteNome);
        }
    }

    public void addProdutoVenda(ProdutoVenda pv) {
        double subtotalProduto = pv.calcularSubtotal();
        int pts = vendaController.getPontosPorProduto(pv.getProduto());

        produtosModel.addElement(pv.getProduto().getNome() +
                " x" + pv.getQuantidade() +
                " - R$ " + String.format("%.2f", subtotalProduto) +
                " (+" + pts + " pts)");

        produtosVenda.add(pv);
        pontos.add(pts);
        subtotal += subtotalProduto;
        totalPontos += pts;
        atualizarInfo();
    }

    public void removerProduto(int index) {
        ProdutoVenda pv = produtosVenda.get(index);
        subtotal -= pv.calcularSubtotal();
        totalPontos -= pontos.get(index);

        produtosVenda.remove(index);
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
        produtosVenda.clear();
        pontos.clear();
        subtotal = 0.0;
        totalPontos = 0;
        atualizarInfo();
    }

    private void finalizarVenda() {
        String clienteNome = (String) clienteComboBox.getSelectedItem();

        if (clienteNome == null || clienteNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (produtosVenda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = vendaController.finalizarVenda(clienteNome, produtosVenda, subtotal, totalPontos);
        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Venda finalizada para: " + clienteNome +
                            "\nSubtotal: R$ " + String.format("%.2f", subtotal) +
                            "\nPontos ganhos: " + totalPontos,
                    "Venda Concluída",
                    JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}





