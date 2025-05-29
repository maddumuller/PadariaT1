package Dao;

import Model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {
    private Connection connection;

    public ProdutoDao(Connection conexao) {
        this.connection = conexao;
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

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome=?, preco=?, tipo=?, quantidade_estoque=?, resgatavel=?, custo_pontos=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getTipo());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setBoolean(5, produto.isResgatavel());
            stmt.setInt(6, produto.getCustoPontos());
            stmt.setInt(7, produto.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarProduto(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setTipo(rs.getString("tipo"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setResgatavel(rs.getBoolean("resgatavel"));
                produto.setCustoPontos(rs.getInt("custo_pontos"));
                produtos.add(produto);
            }
        }
        return produtos;
    }
}


