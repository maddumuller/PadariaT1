package View;

import Controller.ClienteController;
import Model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClienteView extends JFrame {
    private JTextField nomeField, cpfField, telefoneField, pontosField;
    private JButton salvarButton, atualizarButton, deletarButton, limparButton, voltarButton;
    private JTable tabela;
    private DefaultTableModel tabelaModelo;
    private ClienteController controller;
    private int idSelecionado = -1;

    public ClienteView(ClienteController controller) {
        this.controller = controller;

        setTitle("Cadastro de Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

        nomeField = new JTextField();
        cpfField = new JTextField();
        telefoneField = new JTextField();
        pontosField = new JTextField("0");
        pontosField.setEditable(false);

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Total de Pontos:"));
        formPanel.add(pontosField);

        // Botões de ação
        salvarButton = new JButton("Cadastrar");
        atualizarButton = new JButton("Atualizar");
        deletarButton = new JButton("Excluir");
        limparButton = new JButton("Limpar");
        voltarButton = new JButton("Voltar");

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.add(salvarButton);
        botoesPanel.add(atualizarButton);
        botoesPanel.add(deletarButton);
        botoesPanel.add(limparButton);
        botoesPanel.add(voltarButton);

        // Tabela
        tabelaModelo = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Telefone", "Pontos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela não editável diretamente
            }
        };
        tabela = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Adiciona componentes ao frame
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        carregarClientes();

        // Eventos dos botões
        salvarButton.addActionListener(e -> cadastrarCliente());
        atualizarButton.addActionListener(e -> atualizarCliente());
        deletarButton.addActionListener(e -> deletarCliente());
        limparButton.addActionListener(e -> limparCampos());
        voltarButton.addActionListener(e -> voltarParaMenuPrincipal());

        // Clique na tabela carrega dados no formulário
        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    idSelecionado = (int) tabelaModelo.getValueAt(linha, 0);
                    nomeField.setText((String) tabelaModelo.getValueAt(linha, 1));
                    cpfField.setText((String) tabelaModelo.getValueAt(linha, 2));
                    telefoneField.setText((String) tabelaModelo.getValueAt(linha, 3));
                    pontosField.setText(String.valueOf(tabelaModelo.getValueAt(linha, 4)));
                }
            }
        });
    }

    private void cadastrarCliente() {
        try {
            controller.cadastrarCliente(
                    nomeField.getText(),
                    cpfField.getText(),
                    telefoneField.getText(),
                    0 // novos clientes começam com 0 pontos
            );
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            limparCampos();
            carregarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
        }
    }

    private void atualizarCliente() {
        try {
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String telefone = telefoneField.getText();
            int pontos = Integer.parseInt(pontosField.getText());

            int id = controller.buscarIdPorCpf(cpf);
            if (id == -1) {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado para atualizar.");
                return;
            }

            Cliente cliente = new Cliente(id, nome, cpf, telefone, pontos);
            String resposta = controller.atualizarCliente(cliente);
            JOptionPane.showMessageDialog(this, resposta);
            limparCampos();
            carregarClientes();  // Atualiza a tabela
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Pontos inválidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage());
        }
    }


    private void deletarCliente() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este cliente?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deletarCliente(idSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                limparCampos();
                carregarClientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }

    private void carregarClientes() {
        try {
            List<Cliente> clientes = controller.listarClientes();
            tabelaModelo.setRowCount(0);
            for (Cliente c : clientes) {
                tabelaModelo.addRow(new Object[]{
                        c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getPontos()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        cpfField.setText("");
        telefoneField.setText("");
        pontosField.setText("0");
        idSelecionado = -1;
        tabela.clearSelection();
    }

    private void voltarParaMenuPrincipal() {
        dispose();
        new MenuPrincipal().setVisible(true);
    }
}
