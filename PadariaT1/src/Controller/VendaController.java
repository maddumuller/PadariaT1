package Controller;

import Dao.VendaDao;
import Model.Cliente;
import Model.ProdutoVenda;
import Model.Venda;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VendaController {
    private final VendaDao vendaDAO;

    public VendaController(Connection connection) {
        this.vendaDAO = new VendaDao(connection);
    }

    public int cadastrarVenda(Cliente cliente, List<ProdutoVenda> produtos) {
        Venda venda = new Venda();
        venda.registrarVenda(cliente, produtos);
        try {
            return vendaDAO.salvarVenda(venda);
        } catch (SQLException e) {
            System.err.println("Erro ao salvar venda: " + e.getMessage());
            return -1;
        }
    }

    public List<Venda> listarVendas() {
        return vendaDAO.listarTodas();
    }

    public Venda buscarVendaPorId(int id) {
        return vendaDAO.buscarPorId(id);
    }

    public List<Venda> listarVendasPorCliente(int clienteId) {
        return vendaDAO.buscarPorCliente(clienteId);
    }

    public void marcarVendaComoPaga(int id) {
        Venda venda = vendaDAO.buscarPorId(id);
        if (venda != null && !venda.isPago()) {
            venda.marcarComoPago();
            vendaDAO.atualizar(venda, id); // assume que o método atualiza status, pontos e valorTotal
        }
    }

    public void removerVenda(int id) {
        vendaDAO.remover(id);
    }

    public void atualizarVenda(Venda venda, int id) {
        venda.somarValorTotal();
        vendaDAO.atualizar(venda, id);
    }

    public List<Venda> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaDAO.buscarPorPeriodo(inicio, fim);
    }
}
