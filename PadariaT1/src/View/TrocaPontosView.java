package View;

import Controller.ClienteController;
import Controller.ProdutoController;
import Model.Cliente;
import Model.Produto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class TrocaPontosView extends JFrame {
    private JComboBox<String> clienteComboBox;
    private JComboBox<String> produtoComboBox;
    private JButton trocarButton;
    private JButton voltarButton;
    private JLabel pontosDisponiveisLabel;

    private List<Cliente> clientes;
    private List<Produto> produtos;

    private ClienteController clienteController;
    private ProdutoController produtoController;

    public TrocaPontosView(List<Cliente> clientes, List<Produto> produtos) {
        this.clientes = clientes;
        this.produtos = produtos;
        this.clienteController = clienteController;
        this.produtoController = produtoController;

        setTitle("Troca de Pontos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel tituloLabel = new JLabel("Troca de Pontos", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(tituloLabel, BorderLayout.NORTH);

        // Painel central
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 30, 10, 30),
                BorderFactory.createTitledBorder("Informações da Troca")
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // Cliente
        JLabel clienteLabel = new JLabel("Cliente:");
        clienteLabel.setFont(labelFont);
        centerPanel.add(clienteLabel, gbc);

        gbc.gridx = 1;
        clienteComboBox = new JComboBox<>();
        centerPanel.add(clienteComboBox, gbc);

        // Pontos disponíveis
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel pontosLabel = new JLabel("Pontos disponíveis:");
        pontosLabel.setFont(labelFont);
        centerPanel.add(pontosLabel, gbc);

        gbc.gridx = 1;
        pontosDisponiveisLabel = new JLabel("0");
        pontosDisponiveisLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(pontosDisponiveisLabel, gbc);

        // Produto resgatável
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel produtoLabel = new JLabel("Produto Resgatável:");
        produtoLabel.setFont(labelFont);
        centerPanel.add(produtoLabel, gbc);

        gbc.gridx = 1;
        produtoComboBox = new JComboBox<>();
        centerPanel.add(produtoComboBox, gbc);

        // Botão "Trocar"
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        trocarButton = new JButton("Trocar");
        trocarButton.setPreferredSize(new Dimension(150, 35));
        centerPanel.add(trocarButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Rodapé com botão "Voltar"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        voltarButton = new JButton("Voltar");
        voltarButton.setPreferredSize(new Dimension(120, 30));
        bottomPanel.add(voltarButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ação do botão voltar
        voltarButton.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        // Preencher combo boxes
        for (Cliente c : clientes) {
            clienteComboBox.addItem(c.getNome());
        }

        for (Produto p : produtos) {
            produtoComboBox.addItem(p.getNome());
        }

        // Atualizar pontos ao trocar de cliente
        clienteComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Cliente cliente = getClienteSelecionado();
                if (cliente != null) {
                    setPontosDisponiveis(cliente.getPontos());
                }
            }
        });

        // Selecionar primeiro cliente automaticamente
        if (!clientes.isEmpty()) {
            clienteComboBox.setSelectedIndex(0);
            setPontosDisponiveis(clientes.get(0).getPontos());
        }

        // Trocar pontos
        trocarButton.addActionListener(e -> {
            Cliente cliente = getClienteSelecionado();
            Produto produto = getProdutoSelecionado();

            if (cliente == null || produto == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente e um produto.");
                return;
            }

            int pontosCliente = cliente.getPontos();
            int pontosProduto = produto.getCustoPontos();

            if (pontosCliente < pontosProduto) {
                JOptionPane.showMessageDialog(this, "Pontos insuficientes para resgatar este produto.");
                return;
            }

            // Realiza a troca
            cliente.setPontos(pontosCliente - pontosProduto);
            try {
                clienteController.atualizarCliente(cliente); // Atualiza no banco
                setPontosDisponiveis(cliente.getPontos());   // Atualiza visualmente
                JOptionPane.showMessageDialog(this, "Produto resgatado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar pontos do cliente: " + ex.getMessage());
            }
        });
    }

    private Cliente getClienteSelecionado() {
        String nomeSelecionado = (String) clienteComboBox.getSelectedItem();
        for (Cliente c : clientes) {
            if (c.getNome().equals(nomeSelecionado)) {
                return c;
            }
        }
        return null;
    }

    private Produto getProdutoSelecionado() {
        String nomeProduto = (String) produtoComboBox.getSelectedItem();
        for (Produto p : produtos) {
            if (p.getNome().equals(nomeProduto)) {
                return p;
            }
        }
        return null;
    }

    public void setPontosDisponiveis(int pontos) {
        pontosDisponiveisLabel.setText(String.valueOf(pontos));
    }
}

