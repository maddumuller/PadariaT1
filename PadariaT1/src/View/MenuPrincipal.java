package View;

import Controller.ClienteController;
import Controller.ProdutoController;

import javax.swing.*;
import java.awt.event.*;

import Controller.VendaController;
import Dao.ClienteDao;
import Dao.ConexaoBD;
import Dao.ProdutoDao;
import java.sql.Connection;

public class MenuPrincipal extends JFrame {

    private JButton produtoButton;
    private JButton clienteButton;
    private JButton vendaButton;
    private JButton sairButton;
    private Connection conexao;
    private JButton trocaPontosButton;
    private ProdutoController produtoController;
    private ClienteController clienteController;
    private VendaController vendaController;

    public MenuPrincipal() {;
        setTitle("Menu Principal - PadariaT1");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        try {
            this.conexao = ConexaoBD.conectar();
            ProdutoDao produtoDao = new ProdutoDao(conexao);
            ClienteDao clienteDao = new ClienteDao(conexao);
            VendaController vendaDao = new VendaController(conexao);
            this.produtoController = new ProdutoController(produtoDao);
            this.clienteController = new ClienteController(conexao);
            this.vendaController = new VendaController(conexao);

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

        trocaPontosButton = new JButton("Troca de pontos");
        trocaPontosButton.setBounds(100, 200, 200, 30);
        add(trocaPontosButton);

        sairButton = new JButton("Sair");
        sairButton.setBounds(100, 250, 200, 30);
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
                 VendaView vendaView = new VendaView();
                 vendaView.setVisible(true);
            }
        });
        trocaPontosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 TrocaPontosView trocaPontosView = new TrocaPontosView();
                trocaPontosView.setVisible(true);
            }
        });
        sairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.setVisible(true);
    }

}

