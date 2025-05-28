package Dao;

import Model.ProdutoVenda;
import Model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDao {

    private Connection connection;

    public VendaDao(Connection connection) {
        this.connection = connection;
    }

    public void salvarVenda(Venda venda) throws SQLException {
        String insertVendaSQL = "INSERT INTO vendas (cliente_id, data, valor_total, is_pago) VALUES (?, ?, ?, ?) RETURNING id";
        String insertItemSQL = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        String updateEstoqueSQL = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        String updatePontosSQL = "UPDATE clientes SET pontos = pontos + ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            // 1. Inserir a venda
            PreparedStatement vendaStmt = connection.prepareStatement(insertVendaSQL);
            vendaStmt.setInt(1, venda.getCliente().getId());
            vendaStmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            vendaStmt.setDouble(3, venda.getValorTotal());
            vendaStmt.setBoolean(4, venda.isPago());

            ResultSet rs = vendaStmt.executeQuery();
            int vendaId = -1;
            if (rs.next()) {
                vendaId = rs.getInt("id");
            }

            // 2. Inserir os produtos vendidos
            for (ProdutoVenda pv : venda.getProdutos()) {
                PreparedStatement itemStmt = connection.prepareStatement(insertItemSQL);
                itemStmt.setInt(1, vendaId);
                itemStmt.setInt(2, pv.getProduto().getId());
                itemStmt.setInt(3, pv.getQuantidade());
                itemStmt.setDouble(4, pv.getProduto().getPreco());
                itemStmt.executeUpdate();

                // 3. Atualizar estoque
                PreparedStatement estoqueStmt = connection.prepareStatement(updateEstoqueSQL);
                estoqueStmt.setInt(1, pv.getQuantidade());
                estoqueStmt.setInt(2, pv.getProduto().getId());
                estoqueStmt.executeUpdate();
            }

            // 4. Se pago, acumular pontos no cliente
            if (venda.isPago()) {
                int pontos = (int) (venda.getValorTotal() / 10.0);
                PreparedStatement pontosStmt = connection.prepareStatement(updatePontosSQL);
                pontosStmt.setInt(1, pontos);
                pontosStmt.setInt(2, venda.getCliente().getId());
                pontosStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }


}
