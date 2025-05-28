package Model;

public class ProdutoVenda {
    private Produto produto;
    private int quantidade;

    public ProdutoVenda() {}

    public ProdutoVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double calcularSubtotal() {
        if (produto != null) {
            return produto.getPreco() * quantidade;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "ProdutoVenda{" +
                "produto=" + (produto != null ? produto.getNome() : "null") +
                ", quantidade=" + quantidade +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}
