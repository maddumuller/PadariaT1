package Dao;

import Model.Cliente;
import java.sql.*;

public class ClienteDao {
    private Connection connection;

    public ClienteDao(Connection connection) {
        this.connection = connection;
    }

    public void adicionarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf, telefone, pontos) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getPontos());
            stmt.executeUpdate();
        }
    }


    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, telefone = ?, pontos = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getPontos());
            stmt.setInt(5, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void atualizarPontos(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET pontos = ? WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getPontos());
            stmt.setString(2, cliente.getCpf());
            stmt.executeUpdate();
        }
    }

    public void deletarCliente(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Método para listar clientes (exemplo)
    public ResultSet listarClientes() throws SQLException {
        String sql = "SELECT * FROM cliente";
        PreparedStatement stmt = connection.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public Cliente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            rs.getInt("pontos")
                    );
                    cliente.setId(rs.getInt("id"));
                    return cliente;
                }
            }
        }
        return null;
    }
}
