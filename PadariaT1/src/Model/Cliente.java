package Model;

// Classes model sao presentes apenas a criacao das entidades (Fazer seguindo os padroes presentes no diagrama)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private int pontos;

    // Construtor vazio necessário
    public Cliente() {
    }

    // Construtor sem id (para novos clientes antes de salvar no banco)
    public Cliente(String nome, String cpf, String telefone, int pontos) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.pontos = pontos;
    }

    // Construtor com id (para clientes vindos do banco)
    public Cliente(int id, String nome, String cpf, String telefone, int pontos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.pontos = pontos;
    }

    // Metodos de negócio
    public void adicionarPontos(double valor) {
        int pontosGanhos = (int) (valor / 10);
        this.pontos += pontosGanhos;
    }

    public void trocarPontos(Produto produto) {
        if (!produto.isResgatavel()) {
            throw new IllegalStateException("Produto não é resgatável.");
        }
        if (this.pontos < produto.getCustoPontos()) {
            throw new IllegalStateException("Pontos insuficientes para resgatar o produto.");
        }
        this.pontos -= produto.getCustoPontos();
    }

    public void visualizarCliente() {
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Telefone: " + telefone);
        System.out.println("Pontos: " + pontos);
    }

    public void editarCliente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
        // Atualizar dados no banco de dados (se necessário)
    }

    public void removerCliente(Connection conn) throws SQLException {
        String sql = "DELETE FROM cliente WHERE cpf = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.cpf);
            pstmt.executeUpdate();
        }
    }

    public void cadastrarCliente(Connection conn) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf, telefone, pontos) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.nome);
            pstmt.setString(2, this.cpf);
            pstmt.setString(3, this.telefone);
            pstmt.setInt(4, this.pontos);
            pstmt.executeUpdate();
        }
    }

    public static List<Cliente> listarClientes(Connection conn) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getInt("pontos")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    // ===== GETTERS =====
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public int getPontos() {
        return pontos;
    }

    // ===== SETTERS =====
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    @Override
    public String toString() {
        return nome + " - CPF: " + cpf;
    }
}