package Testes;

import Controller.ProdutoController;
import Dao.ProdutoDao;
import View.ProdutoView;
import Dao.ConexaoBD;

import java.sql.Connection;

public class TesteCadastroProdutos {
    public static void main(String[] args) {
        try {
            // Estabelece conexão com o banco
            Connection conn = ConexaoBD.conectar();

            // Cria DAO e Controller
            ProdutoDao produtoDao = new ProdutoDao(conn);
            ProdutoController controller = new ProdutoController(produtoDao);

            // Abre a interface gráfica
            new ProdutoView(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

