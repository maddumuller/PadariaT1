package Controller;

import Dao.ClienteDao;
import Dao.ProdutoDao;
import Dao.VendaDao;
import Model.Cliente;
import Model.Produto;
import Model.ProdutoVenda;
import Model.Venda;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendaController {
    private ClienteDao clienteDao;
    private ProdutoDao produtoDao;
    private VendaDao vendaDao;

    public VendaController(Connection conexao) {
        this.clienteDao = new ClienteDao(conexao);
        this.produtoDao = new ProdutoDao(conexao);
        this.vendaDao = new VendaDao(conexao);
    }

    // Retorna nomes dos clientes para popular o JComboBox
    public List<String> getClientesNomes() {
        List<String> nomes = new ArrayList<>();
        try {
            for (Cliente cliente : clienteDao.listarClientes()) {
                nomes.add(cliente.getNome());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomes;
    }

    // Retorna pontos por produto (ex: 1 ponto por R$10)
    public int getPontosPorProduto(Produto produto) {
        return (int) produto.getPreco() / 10;
    }

    // Diálogo para escolher produto e quantidade
    public ProdutoVenda selecionarProdutoDialog(JFrame parent) {
        List<Produto> produtos = null;
        try {
            produtos = produtoDao.listarProdutos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nenhum produto disponível.");
            return null;
        }

        String[] nomes = produtos.stream().map(Produto::getNome).toArray(String[]::new);
        String selecionado = (String) JOptionPane.showInputDialog(
                parent, "Selecione um produto:", "Escolher Produto",
                JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]
        );

        if (selecionado == null) return null;

        Produto produtoSelecionado = produtos.stream()
                .filter(p -> p.getNome().equals(selecionado))
                .findFirst().orElse(null);

        if (produtoSelecionado == null) return null;

        String qtdStr = JOptionPane.showInputDialog(parent, "Quantidade:");
        if (qtdStr == null || qtdStr.isEmpty()) return null;

        try {
            int quantidade = Integer.parseInt(qtdStr);
            if (quantidade <= 0) throw new NumberFormatException();
            return new ProdutoVenda(produtoSelecionado, quantidade);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Quantidade inválida.");
            return null;
        }
    }

    // Finaliza a venda
    public boolean finalizarVenda(String nomeCliente, List<ProdutoVenda> produtos, double subtotal, int pontosGanhos) {
        try {
            Cliente cliente = clienteDao.buscarPorNome(nomeCliente);
            if (cliente == null) return false;

            Venda venda = new Venda(cliente);
            venda.setProdutos(produtos);
            venda.setValorTotal(subtotal);
            venda.setPago(true);

            boolean sucesso = vendaDao.registrarVenda(venda);

            if (sucesso) {
                cliente.setPontos(cliente.getPontos() + pontosGanhos);
                clienteDao.atualizarCliente(cliente);
            }

            return sucesso;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


