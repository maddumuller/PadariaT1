package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Controller.ClienteController;
import Controller.ProdutoController;

public class ClienteView extends JFrame {
    private MenuPrincipal menuPrincipal;
    private JTextField nomeField, cpfField, telefoneField, pontosField;
    private JButton salvarButton, limparButton, voltarButton;
    private ClienteController controller;

    // Construtor que recebe a tela principal
    public ClienteView() {

        this.menuPrincipal = menuPrincipal;

        setTitle("Cadastro de Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Total de Pontos:"));
        pontosField = new JTextField("0");
        pontosField.setEditable(false);
        formPanel.add(pontosField);

        add(formPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        salvarButton = new JButton("Salvar");
        limparButton = new JButton("Limpar");
        voltarButton = new JButton("Voltar");

        buttonPanel.add(salvarButton);
        buttonPanel.add(limparButton);
        buttonPanel.add(voltarButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Ação do botão Salvar
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = nomeField.getText();
                    String cpf = cpfField.getText();
                    String telefone = telefoneField.getText();
                    int pontos = Integer.parseInt(pontosField.getText());

                    if (controller != null) {
                        controller.cadastrarCliente(nome, cpf, telefone, pontos);
                        JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                    }

                    limparCampos();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
                }
            }
        });

        // Ação do botão Limpar
        limparButton.addActionListener(e -> limparCampos());

        // Ação do botão Voltar
        voltarButton.addActionListener(e -> voltarParaMenuPrincipal());
    }

    private void voltarParaMenuPrincipal() {
        dispose(); // Fecha a janela atual
        if (menuPrincipal != null) {
            menuPrincipal.setVisible(true); // Mostra a tela principal
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        cpfField.setText("");
        telefoneField.setText("");
        pontosField.setText("0");
    }

    public void setController(ClienteController controller) {
        this.controller = controller;
    }

    // Removido o main(), pois essa tela deve ser chamada pela MenuPrincipal
}

