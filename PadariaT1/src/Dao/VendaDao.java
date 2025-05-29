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

    public VendaDao(Connection connection) {
        this.connection = connection;
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
            throw new RuntimeException(e);
        }
    }

    public int inserir(Venda venda) {
        String sql = "INSERT INTO venda (cliente_id, is_pago, valor_total, data_venda) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getCliente().getId());
            stmt.setBoolean(2, venda.isPago());
            stmt.setDouble(3, venda.getValorTotal());
            stmt.setTimestamp(4, Timestamp.valueOf(venda.getDataVenda()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int vendaId = rs.getInt(1);
                inserirProdutosDaVenda(vendaId, venda.getProdutos());
                return vendaId;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public Venda buscarPorId(int id) {
        String sql = "SELECT v.id, v.cliente_id, v.is_pago, v.valor_total, v.data_venda, " +
                "c.nome, c.cpf, c.telefone, c.pontos " +
                "FROM venda v JOIN cliente c ON v.cliente_id = c.id WHERE v.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setPontos(rs.getInt("pontos"));

                Venda venda = new Venda();
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(rs.getInt("id")));
                venda.somarValorTotal();
                return venda;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setPontos(rs.getInt("pontos"));

                Venda venda = new Venda();
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(rs.getInt("id")));
                venda.somarValorTotal();
                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setPontos(rs.getInt("pontos"));

                Venda venda = new Venda();
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(rs.getInt("id")));
                venda.somarValorTotal();
                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public void marcarComoPago(int id) {
        String sql = "UPDATE venda SET is_pago = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM venda WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setPontos(rs.getInt("pontos"));

                Venda venda = new Venda();
                venda.setCliente(cliente);
                venda.setPago(rs.getBoolean("is_pago"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setProdutos(buscarProdutosDaVenda(rs.getInt("id")));
                venda.somarValorTotal();
                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vendas;
    }

    // Métodos auxiliares para gerenciar os produtos da venda
    private void inserirProdutosDaVenda(int vendaId, List<ProdutoVenda> produtos) {
        String sql = "INSERT INTO produto_venda (venda_id, produto_id, quantidade) VALUES (?, ?, ?)";
        for (ProdutoVenda produtoVenda : produtos) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, vendaId);
                stmt.setInt(2, produtoVenda.getProduto().getId());
                stmt.setInt(3, produtoVenda.getQuantidade());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
            throw new RuntimeException(e);
        }
        return produtos;
    }

    private void removerProdutosDaVenda(int vendaId) {
        String sql = "DELETE FROM produto_venda WHERE venda_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
