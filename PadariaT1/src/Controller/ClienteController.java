package Controller;

//Classes Controller sao presentes apenas a criaçao da parte logica, logo as classes presentes em cada entidade(Fazer seguindo os padroes presentes no diagrama)
import Dao.ClienteDao;
import Model.Cliente;
import Model.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClienteController {
    private Connection conn;
    private ClienteDao clienteDao;

    public ClienteController(Connection conn) {
        this.conn = conn;
        this.clienteDao = new ClienteDao(conn);
    }

    public String cadastrarCliente(String nome, String cpf, String telefone, int pontos) {
        try {
            Cliente cliente = new Cliente(nome, cpf, telefone, pontos);
            cliente.cadastrarCliente(conn);
            return "Cliente cadastrado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao cadastrar cliente: " + e.getMessage();
        }
    }

    public Cliente buscarClientePorCpf(String cpf) {
        try {
            List<Cliente> cliente = Cliente.listarClientes(conn);
            for (Cliente c : cliente) {
                if (c.getCpf().equals(cpf)) {
                    return c;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public String adicionarPontos(String cpf, double valorCompra) {
        Cliente cliente = buscarClientePorCpf(cpf);
        if (cliente == null) return "Cliente não encontrado.";

        cliente.adicionarPontos(valorCompra);
        try {
            clienteDao.atualizarPontos(cliente);
            return "Pontos adicionados com sucesso.";
        } catch (SQLException e) {
            return "Erro ao atualizar pontos: " + e.getMessage();
        }
    }

    public String trocarPontos(String cpf, Produto produto) {
        Cliente cliente = buscarClientePorCpf(cpf);
        if (cliente == null) return "Cliente não encontrado.";

        try {
            cliente.trocarPontos(produto);
            clienteDao.atualizarPontos(cliente);
            produto.removerEstoque(1);
            return "Troca de pontos realizada com sucesso.";
        } catch (IllegalStateException e) {
            return "Erro: " + e.getMessage();
        } catch (SQLException e) {
            return "Erro ao atualizar banco de dados: " + e.getMessage();
        }
    }

    public List<Cliente> listarClientes() {
        try {
            return Cliente.listarClientes(conn);
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
            return null;
        }
    }

    public String removerCliente(String cpf) {
        Cliente cliente = buscarClientePorCpf(cpf);
        if (cliente == null) return "Cliente não encontrado.";
        try {
            cliente.removerCliente(conn);
            return "Cliente removido com sucesso.";
        } catch (SQLException e) {
            return "Erro ao remover cliente: " + e.getMessage();
        }
    }


}
