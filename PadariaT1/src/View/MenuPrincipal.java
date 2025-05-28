package View;

import javax.swing.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {

    private JButton produtoButton;
    private JButton clienteButton;
    private JButton vendaButton;
    private JButton sairButton;

    public MenuPrincipal() {
        setTitle("Menu Principal - PadariaT1");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        produtoButton = new JButton("Cadastro de Produto");
        produtoButton.setBounds(100, 50, 200, 30);
        add(produtoButton);

        clienteButton = new JButton("Cadastro de Cliente");
        clienteButton.setBounds(100, 100, 200, 30);
        add(clienteButton);

        vendaButton = new JButton("Cadastro de Venda");
        vendaButton.setBounds(100, 150, 200, 30);
        add(vendaButton);

        sairButton = new JButton("Sair");
        sairButton.setBounds(100, 200, 200, 30);
        add(sairButton);

        // Ações dos botões
        produtoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProdutoView produtoView = new ProdutoView();
                produtoView.setVisible(true);
            }
        });

        clienteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrir tela de Cliente...");
                // ClienteView clienteView = new ClienteView();
                // clienteView.setVisible(true);
            }
        });

        vendaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrir tela de Venda...");
                // VendaView vendaView = new VendaView();
                // vendaView.setVisible(true);
            }
        });

        sairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new MenuPrincipal().setVisible(true);
    }
}

