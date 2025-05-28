package Controller;

//Classes Controller sao presentes apenas a criaçao da parte logica, logo as classes presentes em cada entidade(Fazer seguindo os padroes presentes no diagrama)


import Model.*;

import java.util.List;

public class PadariaController {
    private Padaria padaria;

    public PadariaController() {
        this.padaria = new Padaria();
    }

    public String cadastrarCliente(String nome, String cpf, String telefone) {
        Cliente cliente = new Cliente(nome, cpf, telefone);
        padaria.getClientes().add(cliente);
        return "Cliente cadastrado com sucesso: " + nome;
    }

    public String cadastrarProduto(String nome, double preco, String tipo, int quantidadeEstoque, boolean resgatavel, int custoPontos) {
        Produto produto = new Produto();
        padaria.getProdutos().add(produto);
        return "Produto cadastrado com sucesso: " + nome;
    }

    public String registrarVenda(String cpfCliente, List<ProdutoVenda> produtosVenda) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com CPF: " + cpfCliente);
        }

        for (ProdutoVenda pv : produtosVenda) {
            Produto produto = pv.getProduto();
            if (produto.getQuantidadeEstoque() < pv.getQuantidade()) {
                throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNome());
            }
        }

        Venda venda = new Venda(cliente, produtosVenda);
        venda.registrarVenda(cliente, produtosVenda);
        padaria.getVendas().add(venda);
        return "Venda registrada com sucesso! Valor total: " + venda.getValorTotal();
    }

    public String trocarPontos(String cpfCliente, String nomeProduto) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com CPF: " + cpfCliente);
        }

        Produto produto = buscarProdutoPorNome(nomeProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado: " + nomeProduto);
        }

        if (!produto.isResgatavel()) {
            throw new IllegalStateException("Produto não é resgatável: " + nomeProduto);
        }

        if (cliente.getPontos() < produto.getCustoPontos()) {
            throw new IllegalStateException("Pontos insuficientes! Necessário: " + produto.getCustoPontos() + ", Disponível: " + cliente.getPontos());
        }

        if (produto.getQuantidadeEstoque() < 1) {
            throw new IllegalStateException("Produto fora de estoque: " + nomeProduto);
        }

        cliente.trocarPontos(produto);
        produto.removerEstoque(1);
        return "Troca realizada com sucesso! Produto: " + nomeProduto + " resgatado por " + cliente.getNome();
    }

    private Cliente buscarClientePorCpf(String cpf) {
        for (Cliente c : padaria.getClientes()) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    private Produto buscarProdutoPorNome(String nome) {
        for (Produto p : padaria.getProdutos()) {
            if (p.getNome().equals(nome)) {
                return p;
            }
        }
        return null;
    }

    public List<Cliente> getClientes() {
        return padaria.getClientes();
    }

    public List<Produto> getProdutos() {
        return padaria.getProdutos();
    }

    public List<Venda> getVendas() {
        return padaria.getVendas();
    }
}

