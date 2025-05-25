package Testes;
import Dao.ConexaoBD;
import java.sql.Connection;

//Validar a possibilidade de fazer a classe testes
//usei pra testar o bd :)

public class Testes {
    public static void main(String[] args) {
        Connection conn = ConexaoBD.conectar();
        if (conn != null) {
            System.out.println("Conexão realizada com sucesso!");
        } else {
            System.out.println(" Falha na conexão.");
        }
    }
}
