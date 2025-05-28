package Controller;

import Dao.VendaDao;
import Model.Cliente;
import Model.ProdutoVenda;
import Model.Venda;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class VendaController {
    private final VendaDao vendaDAO;

    public VendaController(Connection connection) {
        this.vendaDAO = new VendaDAO(connection);
    }

    public int cadastrarVenda(Cliente cliente, List<ProdutoVenda> produtos) {
        Venda venda = new Venda(cliente);
        venda.setProdutos(produtos);
        venda.somarValorTotal();
        venda.setDataVenda(LocalDateTime.now());
        venda.setPago(false);
        return vendaDAO.inserir(venda);
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
        vendaDAO.marcarComoPago(id);
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
