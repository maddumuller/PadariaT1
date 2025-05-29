package Controller;

import Dao.ProdutoDao;
import Model.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoController {
    private ProdutoDao produtoDAO;

    public ProdutoController(ProdutoDao produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    // CREATE
    public void cadastrarProduto(String nome, double preco, String tipo, int quantidadeEstoque, boolean resgatavel, int custoPontos) throws SQLException {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setTipo(tipo);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produto.setResgatavel(resgatavel);
        produto.setCustoPontos(custoPontos);
        produtoDAO.adicionarProduto(produto);
    }

    // READ - Listar todos os produtos
    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listarProdutos();
    }

    // READ - Buscar produto por ID
    public Produto buscarProdutoPorId(int id) throws SQLException {
        return produtoDAO.buscarProdutoPorId(id);
    }

    // UPDATE
    public void atualizarProduto(int id, String nome, double preco, String tipo, int quantidadeEstoque, boolean resgatavel, int custoPontos) throws SQLException {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setTipo(tipo);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produto.setResgatavel(resgatavel);
        produto.setCustoPontos(custoPontos);
        produtoDAO.atualizarProduto(produto);
    }

    // DELETE
    public void deletarProduto(int id) throws SQLException {
        produtoDAO.deletarProduto(id);
    }
}

