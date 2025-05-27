package Model;

//Classes model sao presentes apenas a criaçao das entidades(Fazer seguindo os padroes presentes no diagrama)

import java.util.ArrayList;
import java.util.List;

public class Padaria {
    private List<Cliente> clientes;
    private List<Produto> produtos;
    private List<Venda> vendas;

    public Padaria() {
        this.clientes = new ArrayList<>();
        this.produtos = new ArrayList<>();
        this.vendas = new ArrayList<>();
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Venda> getVendas() {
        return vendas;
    }
}
