package View;
import Controller.ClienteController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class ClienteView extends JFrame {
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField telefoneField;
    private JTextField pontosField;
    private JButton cadastrarButton;

    private ClienteController clienteController;

    public ClienteView(Connection connection) {
        clienteController = new ClienteController(connection);

        setTitle("Cadastro de Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza janela

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        panel.add(cpfField);

        panel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        panel.add(telefoneField);

        panel.add(new JLabel("Pontos:"));
        pontosField = new JTextField();
        panel.add(pontosField);

        cadastrarButton = new JButton("Cadastrar Cliente");
        panel.add(cadastrarButton);

        // botão invisível para manter grid
        panel.add(new JLabel(""));

        add(panel);

        // Evento do botão
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String cpf = cpfField.getText();
                String telefone = telefoneField.getText();
                int pontos; // ver sobre pontos dps

                try {

                    pontos = Integer.parseInt(pontosField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Pontos inválidos. Insira um número inteiro.");
                    return;
                }

                String resultado = clienteController.cadastrarCliente(nome, cpf, telefone, pontos);
                JOptionPane.showMessageDialog(null, resultado);
                limparCampos();
            }
        });

        setVisible(true);
    }

    private void limparCampos() {
        nomeField.setText("");
        cpfField.setText("");
        telefoneField.setText("");
    }
    public static void main(String[] args) {
        try {
            Connection conn = Dao.ConexaoBD.conectar();
            new ClienteView(conn);
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }}


