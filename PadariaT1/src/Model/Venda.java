package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Venda {
    private Cliente cliente;
    private List<ProdutoVenda> produtos;
    private boolean pago;
    private double valorTotal;
    private LocalDateTime dataVenda;

    public Venda() {
        this.produtos = new ArrayList<>();
        this.pago = false;
        this.valorTotal = 0.0;
        this.dataVenda = LocalDateTime.now();
    }

    public Venda(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    /**
     * Registra a venda com cliente e produtos, atualiza valores e estoque.
     */
    public void registrarVenda(Cliente cliente, List<ProdutoVenda> produtos) {
        if (cliente == null || produtos == null || produtos.isEmpty()) {
            throw new IllegalArgumentException("Cliente e lista de produtos não podem ser nulos ou vazios.");
        }

        this.cliente = cliente;
        this.produtos = new ArrayList<>(produtos);
        this.dataVenda = LocalDateTime.now();
        somarValorTotal();
        atualizarEstoqueProdutos();
    }

    /**
     * Soma o valor total da venda com base nos produtos e quantidades.
     */
    public void somarValorTotal() {
        this.valorTotal = produtos.stream()
                .mapToDouble(ProdutoVenda::calcularSubtotal)
                .sum();
    }

    /**
     * Atualiza o estoque de todos os produtos vendidos.
     */
    public void atualizarEstoqueProdutos() {
        for (ProdutoVenda produtoVenda : produtos) {
            Produto produto = produtoVenda.getProduto();
            int quantidadeVendida = produtoVenda.getQuantidade();
            if (produto.getQuantidadeEstoque() >= quantidadeVendida) {
                produto.removerEstoque(quantidadeVendida);
            } else {
                throw new IllegalStateException(String.format(
                        "Estoque insuficiente para o produto: %s. Disponível: %d, Solicitado: %d",
                        produto.getNome(), produto.getQuantidadeEstoque(), quantidadeVendida));
            }
        }
    }

    /**
     * Acumula pontos no cliente apenas se a venda foi paga.
     */
    public void acumularPontos() {
        if (cliente != null && pago) {
            int pontosGanhos = (int) (valorTotal / 10.0);
            cliente.adicionarPontos(pontosGanhos);
        }
    }

    /**
     * Marca a venda como paga e acumula pontos.
     */
    public void marcarComoPago() {
        this.pago = true;
        acumularPontos();
    }

    /**
     * Adiciona um produto à venda.
     */
    public void adicionarProduto(Produto produto, int quantidade) {
        Objects.requireNonNull(produto, "Produto não pode ser nulo.");
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida.");

        for (ProdutoVenda produtoVenda : produtos) {
            if (produtoVenda.getProduto().equals(produto)) {
                produtoVenda.setQuantidade(produtoVenda.getQuantidade() + quantidade);
                somarValorTotal();
                return;
            }
        }
        produtos.add(new ProdutoVenda(produto, quantidade));
        somarValorTotal();
    }

    /**
     * Remove um produto da venda.
     */
    public void removerProduto(Produto produto) {
        produtos.removeIf(pv -> pv.getProduto().equals(produto));
        somarValorTotal();
    }

    // Getters e Setters

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ProdutoVenda> getProdutos() {
        return new ArrayList<>(produtos);
    }

    public void setProdutos(List<ProdutoVenda> produtos) {
        this.produtos = new ArrayList<>(Objects.requireNonNull(produtos));
        somarValorTotal();
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
        if (pago) acumularPontos();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== VENDA ===\n")
                .append("Cliente: ").append(cliente != null ? cliente.getNome() : "N/A").append("\n")
                .append("Data: ").append(dataVenda).append("\n")
                .append("Produtos:\n");
        for (ProdutoVenda pv : produtos) {
            sb.append("  - ").append(pv.getProduto().getNome())
                    .append(" x").append(pv.getQuantidade())
                    .append(" = R$ ").append(String.format("%.2f", pv.calcularSubtotal()))
                    .append("\n");
        }
        sb.append("Valor Total: R$ ").append(String.format("%.2f", valorTotal)).append("\n")
                .append("Status: ").append(pago ? "PAGO" : "PENDENTE").append("\n");
        return sb.toString();
    }
}

