package Model;

//Classes model sao presentes apenas a criaçao das entidades(Fazer seguindo os padroes presentes no diagrama)

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
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    public double getPreco() {
        return preco;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getTipo() {
        return tipo;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setResgatavel(boolean resgatavel) {
        this.resgatavel = resgatavel;
    }
    public boolean getResgatavel() {
        return resgatavel;
    }

    public void setCustoPontos(int custoPontos) {
        this.custoPontos = custoPontos;
    }
    public int getCustoPontos() {
        return custoPontos;
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

    public int getId() {
        return id;
    }
}
