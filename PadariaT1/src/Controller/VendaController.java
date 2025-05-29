package Controller;

import Dao.VendaDao;  // Changed from VendaDAO to VendaDao
import Model.Cliente;
import Model.ProdutoVenda;
import Model.Venda;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class VendaController {
    private final VendaDao vendaDao;  // Changed from VendaDAO to VendaDao

    public VendaController(Connection connection) {
        this.vendaDao = new VendaDao(connection);  // Changed from VendaDAO to VendaDao
    }

    public int cadastrarVenda(Cliente cliente, List<ProdutoVenda> produtos) {
        Venda venda = new Venda(cliente);
        venda.setProdutos(produtos);
        venda.somarValorTotal();
        venda.setDataVenda(LocalDateTime.now());
        venda.setPago(false);
        return vendaDao.inserir(venda);  // Changed from vendaDAO to vendaDao
    }

    public List<Venda> listarVendas() {
        return vendaDao.listarTodas();  // Changed from vendaDAO to vendaDao
    }

    public Venda buscarVendaPorId(int id) {
        return vendaDao.buscarPorId(id);  // Changed from vendaDAO to vendaDao
    }

    public List<Venda> listarVendasPorCliente(int clienteId) {
        return vendaDao.buscarPorCliente(clienteId);  // Changed from vendaDAO to vendaDao
    }

    public void marcarVendaComoPaga(int id) {
        vendaDao.marcarComoPago(id);  // Changed from vendaDAO to vendaDao
    }

    public void removerVenda(int id) {
        vendaDao.remover(id);  // Changed from vendaDAO to vendaDao
    }

    public void atualizarVenda(Venda venda, int id) {
        venda.somarValorTotal();
        vendaDao.atualizar(venda, id);  // Changed from vendaDAO to vendaDao
    }

    public List<Venda> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaDao.buscarPorPeriodo(inicio, fim);  // Changed from vendaDAO to vendaDao
    }
}
