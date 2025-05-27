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
                        ");";


                stmt.executeUpdate(tabelaProduto);
                System.out.println("Tabela produto criada com sucesso!");

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
