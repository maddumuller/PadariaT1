package Dao;

import Model.Cliente;
import java.sql.*;

//Classes DAO sao presentes as clases para manipular os dados do BD

public class ClienteDao {
    private Connection connection;

    public ClienteDao(Connection connection) {
        this.connection = connection;
    }
    public void AdicionarCliente(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO cliente(id, nome, CPF, tELEFONE, pontos) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getPontos());

            stmt.executeUpdate();
        }
    }
}
