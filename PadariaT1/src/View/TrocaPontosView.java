package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TrocaPontosView extends JFrame {
    private JComboBox<String> clienteComboBox;
    private JComboBox<String> produtoComboBox;
    private JButton trocarButton;
    private JLabel pontosDisponiveisLabel;

    public TrocaPontosView() {
        setTitle("Troca de Pontos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        clienteComboBox = new JComboBox<>();
        produtoComboBox = new JComboBox<>();
        trocarButton = new JButton("Trocar");
        pontosDisponiveisLabel = new JLabel("Pontos disponíveis: 0");

        panel.add(new JLabel("Cliente:"));
        panel.add(clienteComboBox);

        panel.add(pontosDisponiveisLabel);

        panel.add(new JLabel("Produto Resgatável:"));
        panel.add(produtoComboBox);

        add(panel, BorderLayout.CENTER);
        add(trocarButton, BorderLayout.SOUTH);
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

    public void addTrocarListener(ActionListener listener) {
        trocarButton.addActionListener(listener);
    }

    public static void main(String[] args) {
        new TrocaPontosView().setVisible(true);
    }
}