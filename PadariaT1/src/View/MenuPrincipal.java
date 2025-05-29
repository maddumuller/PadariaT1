package View;

import Controller.ClienteController;
import Controller.ProdutoController;

import javax.swing.*;
import java.awt.event.*;

import Controller.VendaController;
import Dao.ConexaoBD;
import Dao.ProdutoDao;
import java.sql.Connection;

public class MenuPrincipal extends JFrame {

    private JButton produtoButton;
    private JButton clienteButton;
    private JButton vendaButton;
    private JButton sairButton;
    private Connection conexao;
    private ProdutoController produtoController;
    private ClienteController clienteController;
    private VendaController vendaController;

    public MenuPrincipal() {
        setTitle("Menu Principal - PadariaT1");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        try {
            this.conexao = ConexaoBD.conectar();
            ProdutoDao produtoDao = new ProdutoDao(conexao);
            this.produtoController = new ProdutoController(produtoDao);
            // inicialize seu ClienteController aqui, se necessário
            // this.clienteController = new ClienteController(...);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Erro na conexão com o banco: " + e.getMessage());
            System.exit(1);
        }

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
                ProdutoView produtoView = new ProdutoView(produtoController);
                produtoView.setVisible(true);
            }
        });

        clienteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClienteView clienteView = new ClienteView(); // <- construtor vazio
                clienteView.setController(clienteController); // <- associa o controller (opcional)
                clienteView.setVisible(true);
            }
        });

        vendaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrir tela de Venda...");
                // VendaView vendaView = new VendaView();
                // vendaView.setVisible(true);
            }
        });
        vendaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrir tela de Venda...");
                 VendaView vendaView = new VendaView();
                 vendaView.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        new MenuPrincipal().setVisible(true);
    }
}

