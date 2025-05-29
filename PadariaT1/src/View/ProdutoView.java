package View;
import  View.MenuPrincipal;
import Controller.ProdutoController;
import Model.Produto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProdutoView extends JFrame {
    private JTextField nomeField, precoField, tipoField, quantidadeField, custoPontosField;
    private JCheckBox resgatavelCheckBox;
    private JButton cadastrarButton, atualizarButton, deletarButton, voltarButton;
    private JTable tabela;
    private DefaultTableModel tabelaModelo;
    private ProdutoController controller;
    private int idSelecionado = -1;

    public ProdutoView(ProdutoController controller) {
        this.controller = controller;
        setTitle("Cadastro de Produto");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel principal com padding
        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        // Painel de formulário à esquerda
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Dados do Produto", 0, 0, new Font("Segoe UI", Font.BOLD, 14)));
        contentPane.add(formPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels e campos de texto
        String[] labels = {"Nome:", "Preço:", "Tipo:", "Quantidade em Estoque:", "Resgatável com Pontos:", "Custo em Pontos:"};
        Component[] components = new Component[6];

        nomeField = new JTextField(15);
        precoField = new JTextField(15);
        tipoField = new JTextField(15);
        quantidadeField = new JTextField(15);
        resgatavelCheckBox = new JCheckBox();
        custoPontosField = new JTextField(15);

        components[0] = nomeField;
        components[1] = precoField;
        components[2] = tipoField;
        components[3] = quantidadeField;
        components[4] = resgatavelCheckBox;
        components[5] = custoPontosField;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            formPanel.add(components[i], gbc);
        }

        // Painel dos botões abaixo dos campos
        JPanel botoesPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        botoesPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        cadastrarButton = new JButton("Cadastrar");
        atualizarButton = new JButton("Atualizar");
        deletarButton = new JButton("Excluir");
        voltarButton = new JButton("Voltar");

        // Estilo dos botões
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        cadastrarButton.setFont(btnFont);
        atualizarButton.setFont(btnFont);
        deletarButton.setFont(btnFont);
        voltarButton.setFont(btnFont);

        botoesPanel.add(cadastrarButton);
        botoesPanel.add(atualizarButton);
        botoesPanel.add(deletarButton);
        botoesPanel.add(voltarButton);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        formPanel.add(botoesPanel, gbc);

        // Tabela à direita
        tabelaModelo = new DefaultTableModel(
                new String[]{"ID", "Nome", "Preço", "Tipo", "Estoque", "Resgatável", "Pontos"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };
        tabela = new JTable(tabelaModelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(25);
        tabela.getTableHeader().setReorderingAllowed(false);

        // Ajuste de larguras das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(580, 0));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        carregarProdutos();

        // Eventos
        cadastrarButton.addActionListener(e -> cadastrarProduto());
        atualizarButton.addActionListener(e -> atualizarProduto());
        deletarButton.addActionListener(e -> deletarProduto());
        voltarButton.addActionListener(e -> {
            this.dispose();

            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
        });

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row != -1) {
                    idSelecionado = (int) tabelaModelo.getValueAt(row, 0);
                    nomeField.setText((String) tabelaModelo.getValueAt(row, 1));
                    precoField.setText(String.valueOf(tabelaModelo.getValueAt(row, 2)));
                    tipoField.setText((String) tabelaModelo.getValueAt(row, 3));
                    quantidadeField.setText(String.valueOf(tabelaModelo.getValueAt(row, 4)));
                    resgatavelCheckBox.setSelected((Boolean) tabelaModelo.getValueAt(row, 5));
                    custoPontosField.setText(String.valueOf(tabelaModelo.getValueAt(row, 6)));
                }
            }
        });
    }

    private void cadastrarProduto() {
        try {
            controller.cadastrarProduto(
                    nomeField.getText(),
                    Double.parseDouble(precoField.getText()),
                    tipoField.getText(),
                    Integer.parseInt(quantidadeField.getText()),
                    resgatavelCheckBox.isSelected(),
                    Integer.parseInt(custoPontosField.getText())
            );
            limparCampos();
            carregarProdutos();
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
        }
    }

    private void atualizarProduto() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para atualizar.");
            return;
        }
        try {
            controller.atualizarProduto(
                    idSelecionado,
                    nomeField.getText(),
                    Double.parseDouble(precoField.getText()),
                    tipoField.getText(),
                    Integer.parseInt(quantidadeField.getText()),
                    resgatavelCheckBox.isSelected(),
                    Integer.parseInt(custoPontosField.getText())
            );
            limparCampos();
            carregarProdutos();
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }

    private void deletarProduto() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deletarProduto(idSelecionado);
                limparCampos();
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = controller.listarProdutos();
            tabelaModelo.setRowCount(0); // limpa a tabela
            for (Produto p : produtos) {
                tabelaModelo.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getPreco(), p.getTipo(),
                        p.getQuantidadeEstoque(), p.isResgatavel(), p.getCustoPontos()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        precoField.setText("");
        tipoField.setText("");
        quantidadeField.setText("");
        custoPontosField.setText("");
        resgatavelCheckBox.setSelected(false);
        idSelecionado = -1;
        tabela.clearSelection();
    }
}



