package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Controller.ClienteController;
import Controller.ProdutoController;


public class ClienteView extends JFrame {
    private JTextField nomeField, cpfField, telefoneField, pontosField;
    private JButton salvarButton, limparButton;
    private ClienteController controller;

    public ClienteView() {
        setTitle("Cadastro de Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        panel.add(cpfField);

        panel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        panel.add(telefoneField);

        panel.add(new JLabel("Total de Pontos:"));
        pontosField = new JTextField();
        pontosField.setEditable(false);
        pontosField.setText("0");
        panel.add(pontosField);

        salvarButton = new JButton("Salvar");
        limparButton = new JButton("Limpar");

        panel.add(salvarButton);
        panel.add(limparButton);

        add(panel);
    }

    public String getNome() { return nomeField.getText(); }
    public String getCpf() { return cpfField.getText(); }
    public String getTelefone() { return telefoneField.getText(); }

    public void limparCampos() {
        nomeField.setText("");
        cpfField.setText("");
        telefoneField.setText("");
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String telefone = telefoneField.getText();
            int pontos = Integer.parseInt(pontosField.getText());

            controller.cadastrarCliente(nome,cpf,telefone, pontos);
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");

            // Limpar os campos
            nomeField.setText("");
            cpfField.setText("");
            telefoneField.setText("");
            pontosField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
        }

};


    public void addSalvarListener(ActionListener listener) {
        salvarButton.addActionListener(listener);
    }

    public void addLimparListener(ActionListener listener) {
        limparButton.addActionListener(listener);
    }

    public static void main(String[] args) {
        new ClienteView().setVisible(true);
    }
}
