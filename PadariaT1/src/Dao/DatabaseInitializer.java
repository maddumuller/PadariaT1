package Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseInitializer {
        public static void inicializador() {
            try (Connection conn = ConexaoBD.conectar();
                 Statement stmt = conn.createStatement()) {


                String tabelaProduto = "CREATE TABLE IF NOT EXISTS produto (" +
                        "id SERIAL PRIMARY KEY," +
                        "nome VARCHAR(100) NOT NULL," +
                        "preco DECIMAL(10,2) NOT NULL," +
                        "tipo VARCHAR(50) NOT NULL," +
                        "quantidade_estoque INT NOT NULL DEFAULT 0," +
                        "resgatavel BOOLEAN NOT NULL DEFAULT false" +
                        "custo_pontos INT NOT NULL DEFAULT 0;"+
                        ");";


                stmt.executeUpdate(tabelaProduto);
                System.out.println("Tabela produto criada com sucesso!");

                String tabelaCliente = "CREATE TABLE IF NOT EXISTS cliente(" +
                        "id SERIAL PRIMARY KEY," +
                        "nome VARCHAR(100) NOT NULL," +
                        "CPF VARCHAR(14) NOT NULL UNIQUE," +
                        "tELEFONE VARCHAR(15)," +
                        "quantidade_estoque INT NOT NULL DEFAULT 0," +
                        "resgatavel BOOLEAN NOT NULL DEFAULT false" +
                        ");";

                stmt.executeUpdate(tabelaCliente);
                System.out.println("Tabela cliente criado com sucesso!");

                String tabelaVenda = "CREATE TABLE IF NOT EXISTS venda("+
                        "id SERIAL PRIMARY KEY,"+
                        "cliente_id INT,"+
                        "data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
                        "total_valor NUMERIC(10,2) NOT NULL,"+
                        "pontos_gerados INT NOT NULL,"+
                        "FOREIGN KEY (cliente_id) REFERENCES Cliente(id)"+");";

                stmt.executeUpdate(tabelaVenda);
                System.out.println("Tabela venda criado com sucesso!");

                String tabelaVendaProduto = "CREATE TABLE IF NOT EXISTS Venda_Produto ("+
                        "venda_id INT NOT NULL,"+
                        "produto_id INT NOT NULL,"+
                        "quantidade INT NOT NULL,"+
                        "preco_unitario NUMERIC(10,2) NOT NULL,"+
                        "PRIMARY KEY (venda_id, produto_id),"+
                        "FOREIGN KEY (venda_id) REFERENCES Venda(id),"+
                        "FOREIGN KEY (produto_id) REFERENCES Produto(id)"+
                ");";
                stmt.executeUpdate(tabelaVendaProduto);
                System.out.println("Tabela venda produto criado com sucesso!");

                String tabelaPontos= "CREATE TABLE IF NOT EXISTS Troca_Pontos ("+
                        "id SERIAL PRIMARY KEY,"+
                        "cliente_id INT NOT NULL, "+
                        "produto_id INT NOT NULL," +
                        "quantidade INT NOT NULL,"+
                        "pontos_utilizados INT NOT NULL,"+
                        "data_troca TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
                        "FOREIGN KEY (cliente_id) REFERENCES Cliente(id),"+
                        "FOREIGN KEY (produto_id) REFERENCES Produto(id)"+");";

                stmt.executeUpdate(tabelaPontos);
                System.out.println("Tabela pontos criado com sucesso!");


            } catch (SQLException e) {

                System.err.println("Erro SQL ao inicializar o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }

        }
public static void main(String[] args) {
    System.out.println("Iniciando a verificação/criação das tabelas do banco de dados...");
    inicializador();
    // O método initialize() já imprime mensagens de sucesso ou erro.
}
}
//                System.out.println("Processo de inicialização do banco de dados concluído com êxito.");
