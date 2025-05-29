package Controller;

import Dao.ClienteDao;
import Model.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private ClienteDao clienteDao;

    public ClienteController(Connection connection) {
        this.clienteDao = new ClienteDao(connection);
    }

    // Cadastrar cliente
    public String cadastrarCliente(String nome, String cpf, String telefone, int pontos) {
        try {
            Cliente cliente = new Cliente(nome, cpf, telefone, pontos);
            clienteDao.adicionarCliente(cliente);
            return "Cliente cadastrado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao cadastrar cliente: " + e.getMessage();
        }
    }

    // Atualizar cliente completo (por id)
    public String atualizarCliente(Cliente cliente) {
        try {
            clienteDao.atualizarCliente(cliente);
            return "Cliente atualizado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao atualizar cliente: " + e.getMessage();
        }
    }

    // Buscar cliente por CPF
    public Cliente buscarClientePorCpf(String cpf) {
        try {
            return clienteDao.buscarPorCpf(cpf);
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente por CPF: " + e.getMessage());
            return null;
        }
    }

    // Buscar ID por CPF (utilizando o método acima)
    public int buscarIdPorCpf(String cpf) {
        Cliente cliente = buscarClientePorCpf(cpf);
        return cliente != null ? cliente.getId() : -1;
    }

    // Atualizar apenas os pontos do cliente (pelo CPF)
    public String atualizarPontosCliente(String cpf, int novosPontos) {
        try {
            Cliente cliente = buscarClientePorCpf(cpf);
            if (cliente == null) {
                return "Cliente não encontrado.";
            }
            cliente.setPontos(novosPontos);
            clienteDao.atualizarPontos(cliente);
            return "Pontos atualizados com sucesso!";
        } catch (SQLException e) {
            return "Erro ao atualizar pontos: " + e.getMessage();
        }
    }

    // Remover cliente por id
    public String deletarCliente(int id) {
        try {
            clienteDao.deletarCliente(id);
            return "Cliente removido com sucesso!";
        } catch (SQLException e) {
            return "Erro ao remover cliente: " + e.getMessage();
        }
    }

    // Listar todos os clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        try (ResultSet rs = clienteDao.listarClientes()) {
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getInt("pontos")
                );
                c.setId(rs.getInt("id")); // Corrigido: atribui o ID
                clientes.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }
}
