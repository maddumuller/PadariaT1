package Testes;

import Controller.PadariaController;
import Model.Produto;

public class TestePadaria {

        public static void main(String[] args) {
            PadariaController controller = new PadariaController();

            // Teste de cadastro de produto
            String resultado = controller.cadastrarProduto("Pão", 5.0, "Alimento", 100, true, 10);
            System.out.println(resultado);

            // Listando produtos cadastrados
            for (Produto p : controller.getProdutos()) {
                System.out.println("Produto: " + p.getNome() + ", Preço: " + p.getPreco());
            }
        }
    }


