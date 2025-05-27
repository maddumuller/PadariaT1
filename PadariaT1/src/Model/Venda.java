package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private Cliente cliente;
    private List<ProdutoVenda> produtos;
    private boolean isPago;
    private double valorTotal;
    private LocalDateTime dataVenda;

    public Venda() {
        this.produtos = new ArrayList<>();
        this.isPago = false;
        this.valorTotal = 0.0;
        this.dataVenda = LocalDateTime.now();
    }

    public Venda(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public void registrarVenda(Cliente cliente, List<ProdutoVenda> produtos) {
        this.cliente = cliente;
        this.produtos = new ArrayList<>(produtos);
        this.dataVenda = LocalDateTime.now();
        somarValorTotal();
        atualizarEstoqueProdutos();
        acumularPontos();
    }

    public void somarValorTotal() {
        this.valorTotal = 0.0;
        for (ProdutoVenda produtoVenda : produtos) {
            this.valorTotal += produtoVenda.calcularSubtotal();
        }
    }

    public void atualizarEstoqueProdutos() {
        for (ProdutoVenda produtoVenda : produtos) {
            Produto produto = produtoVenda.getProduto();
            int quantidadeVendida = produtoVenda.getQuantidade();
            if (produto.getQuantidadeEstoque() >= quantidadeVendida) {
                produto.removerEstoque(quantidadeVendida);
            } else {
                throw new IllegalStateException(
                        "Estoque insuficiente para o produto: " + produto.getNome() +
                                ". Disponível: " + produto.getQuantidadeEstoque() +
                                ", Solicitado: " + quantidadeVendida
                );
            }
        }
    }

    public void acumularPontos() {
        if (cliente != null && isPago) {
            int pontosGanhos = (int) (valorTotal / 10.0);
            cliente.adicionarPontos(pontosGanhos);
        }
    }

    public void marcarComoPago() {
        this.isPago = true;
        acumularPontos();
    }

    public void adicionarProduto(Produto produto, int quantidade) {
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

    public void removerProduto(Produto produto) {
        produtos.removeIf(pv -> pv.getProduto().equals(produto));
        somarValorTotal();
    }

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
        this.produtos = new ArrayList<>(produtos);
        somarValorTotal();
    }

    public boolean isPago() {
        return isPago;
    }

    public void setPago(boolean pago) {
        this.isPago = pago;
        if (pago) {
            acumularPontos();
        }
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
        sb.append("=== VENDA ===\n");
        sb.append("Cliente: ").append(cliente != null ? cliente.getNome() : "N/A").append("\n");
        sb.append("Data: ").append(dataVenda).append("\n");
        sb.append("Produtos:\n");
        for (ProdutoVenda pv : produtos) {
            sb.append("  - ").append(pv.getProduto().getNome())
                    .append(" x").append(pv.getQuantidade())
                    .append(" = R$ ").append(String.format("%.2f", pv.calcularSubtotal()))
                    .append("\n");
        }
        sb.append("Valor Total: R$ ").append(String.format("%.2f", valorTotal)).append("\n");
        sb.append("Status: ").append(isPago ? "PAGO" : "PENDENTE").append("\n");
        return sb.toString();
    }
}
