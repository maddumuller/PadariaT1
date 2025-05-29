package Dao;

import Model.Cliente;
import Model.ProdutoVenda;
import Model.Venda;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDao {
    private final Connection connection;

    public VendaDao(Connection connection) {
        this.connection = connection;
    }

    public int salvarVenda(Venda venda) throws SQLException {
        String insertVendaSQL = "INSERT INTO vendas (cliente_id, data, valor_total, is_pago) VALUES (?, ?, ?, ?) RETURNING id";
        String insertItemSQL = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        String updateEstoqueSQL = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        String updatePontosSQL = "UPDATE clientes SET pontos = pontos + ? WHERE id = ?";

        int vendaId = -1;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement vendaStmt = connection.prepareStatement(insertVendaSQL)) {
                vendaStmt.setInt(1, venda.getCliente().getId());
                vendaStmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
                vendaStmt.setDouble(3, venda.getValorTotal());
                vendaStmt.setBoolean(4, venda.isPago());

                try (ResultSet rs = vendaStmt.executeQuery()) {
                    if (rs.next()) {
                        vendaId = rs.getInt("id");
                    }
                }
            }

            for (ProdutoVenda pv : venda.getProdutos()) {
                try (PreparedStatement itemStmt = connection.prepareStatement(insertItemSQL)) {
                    itemStmt.setInt(1, vendaId);
                    itemStmt.setInt(2, pv.getProduto().getId());
                    itemStmt.setInt(3, pv.getQuantidade());
                    itemStmt.setDouble(4, pv.getProduto().getPreco());
                    itemStmt.executeUpdate();
                }

                try (PreparedStatement estoqueStmt = connection.prepareStatement(updateEstoqueSQL)) {
                    estoqueStmt.setInt(1, pv.getQuantidade());
                    estoqueStmt.setInt(2, pv.getProduto().getId());
                    estoqueStmt.executeUpdate();
                }
            }

            if (venda.isPago()) {
                int pontos = (int) (venda.getValorTotal() / 10.0);
                try (PreparedStatement pontosStmt = connection.prepareStatement(updatePontosSQL)) {
                    pontosStmt.setInt(1, pontos);
                    pontosStmt.setInt(2, venda.getCliente().getId());
                    pontosStmt.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }

        return vendaId;
    }

    public List<Venda> listarTodas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM vendas";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = montarVendaBasica(rs);
                vendas.add(venda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendas;
    }

    public Venda buscarPorId(int id) {
        String sql = "SELECT * FROM vendas WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarVendaBasica(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Venda> buscarPorCliente(int clienteId) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM vendas WHERE cliente_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(montarVendaBasica(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendas;
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM vendas WHERE data BETWEEN ? AND ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(montarVendaBasica(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendas;
    }

    public void atualizar(Venda venda, int id) {
        String sql = "UPDATE vendas SET cliente_id = ?, data = ?, valor_total = ?, is_pago = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getCliente().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setDouble(3, venda.getValorTotal());
            stmt.setBoolean(4, venda.isPago());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM vendas WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void marcarComoPago(int id) {
        String sql = "UPDATE vendas SET is_pago = TRUE WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para evitar duplicação de código
    private Venda montarVendaBasica(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setDataVenda(rs.getTimestamp("data").toLocalDateTime());
        venda.setPago(rs.getBoolean("is_pago"));
        venda.setProdutos(new ArrayList<>());
        String nome = rs.getString("cliente_nome");
        String cpf = rs.getString("cliente_cpf");
        String telefone = rs.getString("cliente_telefone");
        int pontos = rs.getInt("cliente_pontos");// Idealmente, carregar produtos separados
        venda.setCliente(new Cliente("nome", "cpf", "telefone", pontos));
        venda.somarValorTotal(); // ou set diretamente, se armazenado
        return venda;
    }
}
