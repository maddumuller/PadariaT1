package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VendaView extends JFrame {
    private JTextField clienteTextField;
    private JList<String> produtosList;
    private DefaultListModel<String> produtosModel;
    private JButton adicionarProdutoButton, finalizarVendaButton, removerProdutoButton;
    private JLabel pontosLabel, subtotalLabel;

    private int totalPontos = 0;
    private double subtotal = 0.0;


    private java.util.List<Double> precos = new java.util.ArrayList<>();
    private java.util.List<Integer> pontos = new java.util.ArrayList<>();

    public VendaView() {
        setTitle("Registrar Venda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));


        JPanel clientePanel = new JPanel(new BorderLayout());
        clientePanel.add(new JLabel("Cliente:"), BorderLayout.WEST);
        clienteTextField = new JTextField();
        clientePanel.add(clienteTextField, BorderLayout.CENTER);
        panel.add(clientePanel, BorderLayout.NORTH);


        produtosModel = new DefaultListModel<>();
        produtosList = new JList<>(produtosModel);
        produtosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(produtosList);
        panel.add(scrollPane, BorderLayout.CENTER);


        JPanel infoPanel = new JPanel(new GridLayout(2,1));
        subtotalLabel = new JLabel("Subtotal: R$ 0.00");
        pontosLabel = new JLabel("Pontos ganhos: 0");
        infoPanel.add(subtotalLabel);
        infoPanel.add(pontosLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);


        JPanel botoesPanel = new JPanel(new FlowLayout());
        adicionarProdutoButton = new JButton("Adicionar Produto");
        removerProdutoButton = new JButton("Remover Produto");
        finalizarVendaButton = new JButton("Finalizar Venda");
        botoesPanel.add(adicionarProdutoButton);
        botoesPanel.add(removerProdutoButton);
        botoesPanel.add(finalizarVendaButton);

        add(panel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);


        removerProdutoButton.addActionListener(e -> {
            int index = produtosList.getSelectedIndex();
            if (index != -1) {
                removerProduto(index);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });


        finalizarVendaButton.addActionListener(e -> finalizarVenda());
    }

    public void addProduto(String nome, double preco, int pts) {
        produtosModel.addElement(nome + " - R$ " + String.format("%.2f", preco) + " (+" + pts + " pts)");
        precos.add(preco);
        pontos.add(pts);
        subtotal += preco;
        totalPontos += pts;
        atualizarInfo();
    }

    public void removerProduto(int index) {
        subtotal -= precos.get(index);
        totalPontos -= pontos.get(index);
        precos.remove(index);
        pontos.remove(index);
        produtosModel.remove(index);

        atualizarInfo();
    }

    private void atualizarInfo() {
        subtotalLabel.setText("Subtotal: R$ " + String.format("%.2f", subtotal));
        pontosLabel.setText("Pontos ganhos: " + totalPontos);
    }

    public void limparCampos() {
        clienteTextField.setText("");
        produtosModel.clear();
        precos.clear();
        pontos.clear();
        subtotal = 0.0;
        totalPontos = 0;
        atualizarInfo();
    }

    private void finalizarVenda() {
        String cliente = clienteTextField.getText().trim();
        JOptionPane.showMessageDialog(
                this,
                "Venda finalizada para: " + cliente +
                        "\nSubtotal: R$ " + String.format("%.2f", subtotal) +
                        "\nPontos ganhos: " + totalPontos,
                "Venda Concluída",
                JOptionPane.INFORMATION_MESSAGE
        );
        limparCampos();
    }

    public static void main(String[] args) {
        VendaView view = new VendaView();
        view.setVisible(true);

    }
}
