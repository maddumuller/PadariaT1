package Model;

import java.time.LocalDateTime;
import java.util.List;

public class Venda {
    private int id;
    private Cliente cliente;
    private boolean isPago;
    private double valorTotal;
    private LocalDateTime dataVenda;
    private List<ProdutoVenda> produtos;

    // Construtor padrão
    public Venda() {
    }

    // Construtor com cliente
    public Venda(Cliente cliente) {
        this.cliente = cliente;
        this.isPago = false;
        this.dataVenda = LocalDateTime.now();
    }

    // Método para somar valor total dos produtos
    public void somarValorTotal() {
        this.valorTotal = 0.0;
        if (produtos != null) {
            for (ProdutoVenda produtoVenda : produtos) {
                this.valorTotal += produtoVenda.getSubtotal();
            }
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isPago() {
        return isPago;
    }

    public void setPago(boolean pago) {
        isPago = pago;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ProdutoVenda> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoVenda> produtos) {
        this.produtos = produtos;
    }

    // Método para visualizar venda
    public void visualizarVenda() {
        System.out.println("=== VENDA ===");
        System.out.println("ID: " + id);
        System.out.println("Cliente: " + (cliente != null ? cliente.getNome() : "N/A"));
        System.out.println("Data: " + dataVenda);
        System.out.println("Valor Total: R$ " + String.format("%.2f", valorTotal));
        System.out.println("Status: " + (isPago ? "PAGO" : "PENDENTE"));

        if (produtos != null && !produtos.isEmpty()) {
            System.out.println("\n=== PRODUTOS ===");
            for (ProdutoVenda pv : produtos) {
                System.out.println("- " + pv.getProduto().getNome() +
                        " | Qtd: " + pv.getQuantidade() +
                        " | Subtotal: R$ " + String.format("%.2f", pv.getSubtotal()));
            }
        }
    }
}