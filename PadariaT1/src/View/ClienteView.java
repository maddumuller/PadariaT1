package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

//Me falem oque vc acharam das view


public class ClienteView extends JFrame {
    private JTextField nomeField, cpfField, telefoneField, pontosField;
    private JButton salvarButton, limparButton;

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
