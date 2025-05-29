package Dao;

import Model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClienteDao {
    private Connection connection;

    public ClienteDao(Connection connection) {
        this.connection = connection;
    }

    public void AdicionarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente(id, nome, CPF, tELEFONE, pontos) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getPontos());

            stmt.executeUpdate();
        }
    }
        public void atualizarPontos(Cliente cliente) throws SQLException {
            String update = "UPDATE cliente SET pontos = ? WHERE cpf = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(update)) {
                pstmt.setInt(1, cliente.getPontos());
                pstmt.setString(2, cliente.getCpf());
                pstmt.executeUpdate();
            }
        }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getInt("pontos")
                );
                cliente.setId(rs.getInt("id"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

}


