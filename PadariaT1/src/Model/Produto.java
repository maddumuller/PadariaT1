package Model;

//Classes model sao presentes apenas a criaçao das entidades(Fazer seguindo os padroes presentes no diagrama)

public class Produto {
    private String nome;
    private double preco;
    private String tipo;
    private int quantidadeEstoque;
    private boolean resgatavel;
    private int custoPontos;

    public Produto(String nome, double preco, String tipo, int quantidadeEstoque, boolean resgatavel, int custoPontos) {
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.quantidadeEstoque = quantidadeEstoque;
        this.resgatavel = resgatavel;
        this.custoPontos = custoPontos;
    }

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
        //
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
        //  método vazio a ser tratado no DAO
    }

    public void visualizarProduto() {
        System.out.println(this);
    }

    public boolean isResgatavel() {
        return resgatavel;
    }

    @Override
    public String toString() {
        return String.format("Produto: %s | Preço: R$%.2f | Tipo: %s | Estoque: %d | Resgatável: %s | Custo em Pontos: %d",
                nome, preco, tipo, quantidadeEstoque, resgatavel ? "Sim" : "Não", custoPontos);
    }

}
