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
    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listarProdutos();
    }

}
