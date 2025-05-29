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

    public Cliente() {}

    public Cliente(String nome, String cpf, String telefone, int pontos) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.pontos = pontos;
    }

    public Cliente(int id, String nome, String cpf, String telefone, int pontos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.pontos = pontos;
    }

    // Regras de negócio
    public void adicionarPontos(double valor) {
        this.pontos += (int) (valor / 10);
    }

    public void trocarPontos(Produto produto) {
        if (!produto.isResgatavel()) {
            throw new IllegalStateException("Produto não é resgatável.");
        }
        if (this.pontos < produto.getCustoPontos()) {
            throw new IllegalStateException("Pontos insuficientes.");
        }
        this.pontos -= produto.getCustoPontos();
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public int getPontos() { return pontos; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setPontos(int pontos) { this.pontos = pontos; }
}

