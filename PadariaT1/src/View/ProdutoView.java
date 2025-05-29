package View;

import Controller.ProdutoController;
import Dao.ProdutoDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class ProdutoView extends JFrame {
        private JTextField nomeField;
        private JTextField precoField;
        private JTextField tipoField;
        private JTextField quantidadeField;
        private JCheckBox resgatavelCheckBox;
        private JTextField custoPontosField;
        private JButton cadastrarButton;
        private ProdutoController controller;

        public ProdutoView(ProdutoController controller) {

            this.controller = controller;
            setTitle("Cadastro de Produto");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 350);
            setLocationRelativeTo(null);

            // Painel principal
            JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Campos de entrada
            panel.add(new JLabel("Nome:"));
            nomeField = new JTextField();
            panel.add(nomeField);

            panel.add(new JLabel("Preço:"));
            precoField = new JTextField();
            panel.add(precoField);

            panel.add(new JLabel("Tipo:"));
            tipoField = new JTextField();
            panel.add(tipoField);

            panel.add(new JLabel("Quantidade em estoque:"));
            quantidadeField = new JTextField();
            panel.add(quantidadeField);

            panel.add(new JLabel("Resgatável com pontos:"));
            resgatavelCheckBox = new JCheckBox();
            panel.add(resgatavelCheckBox);

            panel.add(new JLabel("Custo em pontos:"));
            custoPontosField = new JTextField();
            panel.add(custoPontosField);

            cadastrarButton = new JButton("Cadastrar Produto");
            panel.add(cadastrarButton);

            // espaço para layout
            panel.add(new JLabel());

            add(panel);
            setVisible(true);

            // Ação do botão
            cadastrarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nome = nomeField.getText();
                        double preco = Double.parseDouble(precoField.getText());
                        String tipo = tipoField.getText();
                        int quantidade = Integer.parseInt(quantidadeField.getText());
                        boolean resgatavel = resgatavelCheckBox.isSelected();
                        int custoPontos = Integer.parseInt(custoPontosField.getText());

                        controller.cadastrarProduto(nome, preco, tipo, quantidade, resgatavel, custoPontos);
                        JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

                        // Limpar os campos
                        nomeField.setText("");
                        precoField.setText("");
                        tipoField.setText("");
                        quantidadeField.setText("");
                        resgatavelCheckBox.setSelected(false);
                        custoPontosField.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
                    }
                }
            });
        }
    }

//        public static void main(String[] args) {
//            ProdutoController controller = new ProdutoController();
//            new ProdutoView(controller).setVisible(true);
//        }
//    retirar chave de cima caso for utilizar esta classe }



