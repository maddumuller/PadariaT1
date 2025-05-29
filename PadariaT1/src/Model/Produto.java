package Model;

//Classes model sao presentes apenas a criacao das entidades (Fazer seguindo os padroes presentes no diagrama)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private String tipo;
    private int quantidadeEstoque;
    private boolean resgatavel;
    private int custoPontos;

    public Produto() {

    }

    // ===== Setters e Getters =====

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public boolean getResgatavel() {
        return resgatavel;
    }
    public void setResgatavel(boolean resgatavel) {
        this.resgatavel = resgatavel;
    }

    public int getCustoPontos() {
        return custoPontos;
    }
    public void setCustoPontos(int custoPontos) {
        this.custoPontos = custoPontos;
    }


    // ===== Métodos de Negócio =====

    public void adicionarEstoque(int qtd) {
        this.quantidadeEstoque += qtd;
    }

    public void removerEstoque(int qtd) {
        if (this.quantidadeEstoque >= qtd) {
            this.quantidadeEstoque -= qtd;
        } else {
            throw new IllegalArgumentException("Estoque insuficiente.");
        }
    }

    public void criarProduto() {
        // Método vazio, a ser tratado no DAO se necessário
    }

    public void editarProduto(String nome, double preco, String tipo, int quantidadeEstoque, boolean resgatavel, int custoPontos) {
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.quantidadeEstoque = quantidadeEstoque;
        this.resgatavel = resgatavel;
        this.custoPontos = custoPontos;
    }

    public void removerProduto() {
        // Método vazio, a ser tratado no DAO se necessário
    }

    public void visualizarProduto() {
        System.out.println(this);
    }

    public boolean isResgatavel() {
        return resgatavel;
    }

    @Override
    public String toString() {
        return String.format(
                "Produto: %s | Preço: R$%.2f | Tipo: %s | Estoque: %d | Resgatável: %s | Custo em Pontos: %d",
                nome, preco, tipo, quantidadeEstoque, resgatavel ? "Sim" : "Não", custoPontos
        );
    }
}
