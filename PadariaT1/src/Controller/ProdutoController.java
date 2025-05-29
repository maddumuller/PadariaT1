package Controller;
import Dao.ProdutoDao;
import Model.Produto;
import java.sql.SQLException;
//Classes Controller sao presentes apenas a criaçao da parte logica, logo as classes presentes em cada entidade(Fazer seguindo os padroes presentes no diagrama)

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
}
