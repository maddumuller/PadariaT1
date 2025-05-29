package Dao;

import Model.Produto;
import java.sql.*;


public class ProdutoDao {
    private Connection connection;

    public ProdutoDao(Connection connection) {
        this.connection = connection;
    }
    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco, tipo, quantidade_estoque, resgatavel, custo_pontos) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getTipo());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setBoolean(5, produto.isResgatavel());
            stmt.setInt(6, produto.getCustoPontos());
            stmt.executeUpdate();
        }
    }
}