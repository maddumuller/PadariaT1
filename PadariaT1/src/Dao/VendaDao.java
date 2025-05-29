package Dao;

import Model.Venda;
import Model.Cliente;
import Model.ProdutoVenda;
import Model.Produto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDao {
    private Connection connection;

    public VendaDao(Connection conexao) {
        this.connection = conexao;
    }

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS venda (" +
                "id SERIAL PRIMARY KEY," +
                "cliente_id INTEGER NOT NULL," +
                "is_pago BOOLEAN DEFAULT FALSE," +
                "valor_total DECIMAL(10,2) NOT NULL," +
                "data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (cliente_id) REFERENCES cliente(id)" +
                ")";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela venda", e);
        }
    }

    public boolean registrarVenda(Venda venda) {
        String sql = "INSERT INTO venda (cliente_id, is_pago, valor_total, data_venda) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getCliente().getId());
            stmt.setBoolean(2, venda.isPago());
            stmt.setDouble(3, venda.getValorTotal());
            stmt.setTimestamp(4, Timestamp.valueOf(venda.getDataVenda()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int vendaId = rs.getInt(1);
                venda.setId(vendaId);
                inserirProdutosDaVenda(vendaId, venda.getProdutos());
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar venda", e);
        }
        return false;
    }

    public Venda buscarPorId(int id) {
        String sql = "SELECT v.id, v.cliente_id, v.is_pago, v.valor_total, v.data_venda, " +
                "c.nome, c.cpf, c.telefone, c.pontos " +
                "FROM venda v JOIN cliente c ON v.cliente_id = c.id WHERE v.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = montarCliente(rs);
                Venda venda = new Venda();
                venda.setId(rs.getInt("id")); // importante setar id da venda
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(id));
                venda.somarValorTotal();
                return venda;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar venda por id", e);
        }
        return null;
    }

    public List<Venda> listarTodas() {
        String sql = "SELECT v.id, v.cliente_id, v.is_pago, v.valor_total, v.data_venda, " +
                "c.nome, c.cpf, c.telefone, c.pontos " +
                "FROM venda v JOIN cliente c ON v.cliente_id = c.id ORDER BY v.data_venda DESC";
        List<Venda> vendas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = montarCliente(rs);
                int vendaId = rs.getInt("id");
                Venda venda = new Venda();
                venda.setId(vendaId);
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(vendaId));
                venda.somarValorTotal();
                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas", e);
        }
        return vendas;
    }

    public List<Venda> buscarPorCliente(int clienteId) {
        String sql = "SELECT v.id, v.cliente_id, v.is_pago, v.valor_total, v.data_venda, " +
                "c.nome, c.cpf, c.telefone, c.pontos " +
                "FROM venda v JOIN cliente c ON v.cliente_id = c.id " +
                "WHERE v.cliente_id = ? ORDER BY v.data_venda DESC";
        List<Venda> vendas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = montarCliente(rs);
                int vendaId = rs.getInt("id");
                Venda venda = new Venda();
                venda.setId(vendaId);
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(vendaId));
                venda.somarValorTotal();
                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendas por cliente", e);
        }
        return vendas;
    }

    public void atualizar(Venda venda, int id) {
        String sql = "UPDATE venda SET cliente_id = ?, is_pago = ?, valor_total = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getCliente().getId());
            stmt.setBoolean(2, venda.isPago());
            stmt.setDouble(3, venda.getValorTotal());
            stmt.setInt(4, id);
            stmt.executeUpdate();
            removerProdutosDaVenda(id);
            inserirProdutosDaVenda(id, venda.getProdutos());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar venda", e);
        }
    }

    public void marcarComoPago(int id) {
        String sql = "UPDATE venda SET is_pago = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao marcar venda como paga", e);
        }
    }

    public void remover(int id) {
        // Remover produtos da venda antes para manter integridade
        removerProdutosDaVenda(id);
        String sql = "DELETE FROM venda WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover venda", e);
        }
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT v.id, v.cliente_id, v.is_pago, v.valor_total, v.data_venda, " +
                "c.nome, c.cpf, c.telefone, c.pontos " +
                "FROM venda v JOIN cliente c ON v.cliente_id = c.id " +
                "WHERE v.data_venda BETWEEN ? AND ? ORDER BY v.data_venda DESC";
        List<Venda> vendas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = montarCliente(rs);
                int vendaId = rs.getInt("id");
                Venda venda = new Venda();
                venda.setId(vendaId);
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(vendaId));
                venda.somarValorTotal();
                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendas por período", e);
        }
        return vendas;
    }

    // Métodos auxiliares para gerenciar os produtos da venda
    private void inserirProdutosDaVenda(int vendaId, List<ProdutoVenda> produtos) {
        String sql = "INSERT INTO produto_venda (venda_id, produto_id, quantidade) VALUES (?, ?, ?)";
        try {
            for (ProdutoVenda produtoVenda : produtos) {
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, vendaId);
                    stmt.setInt(2, produtoVenda.getProduto().getId());
                    stmt.setInt(3, produtoVenda.getQuantidade());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produtos da venda", e);
        }
    }

    private List<ProdutoVenda> buscarProdutosDaVenda(int vendaId) {
        String sql = "SELECT pv.produto_id, pv.quantidade, p.nome, p.preco, p.tipo, p.quantidadeEstoque, p.resgatavel, p.custoPontos " +
                "FROM produto_venda pv " +
                "JOIN produto p ON pv.produto_id = p.id " +
                "WHERE pv.venda_id = ?";
        List<ProdutoVenda> produtos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setTipo(rs.getString("tipo"));
                produto.setQuantidadeEstoque(rs.getInt("quantidadeEstoque"));
                produto.setResgatavel(rs.getBoolean("resgatavel"));
                produto.setCustoPontos(rs.getInt("custoPontos"));
                int quantidade = rs.getInt("quantidade");
                produtos.add(new ProdutoVenda(produto, quantidade));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos da venda", e);
        }
        return produtos;
    }

    private void removerProdutosDaVenda(int vendaId) {
        String sql = "DELETE FROM produto_venda WHERE venda_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produtos da venda", e);
        }
    }

    private Cliente montarCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setPontos(rs.getInt("pontos"));
        return cliente;
    }
}
