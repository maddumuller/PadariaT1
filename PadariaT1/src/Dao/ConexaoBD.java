package Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class ConexaoBD {
    private static String URL;
    private static String USUARIO;
    private static String SENHA;

    static {
        try {
            Properties props = new Properties();
            InputStream input = ConexaoBD.class.getClassLoader().getResourceAsStream("db.properties");

            if (input == null) {
                throw new IOException("Arquivo db.properties não encontrado no classpath.");
            }

            props.load(input);
            URL = props.getProperty("url");
            USUARIO = props.getProperty("usuario");
            SENHA = props.getProperty("senha");

            if (URL == null || USUARIO == null || SENHA == null) {
                throw new IOException("Faltando uma ou mais propriedades: url, usuario, senha.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo db.properties: " + e.getMessage());
            throw new RuntimeException("Falha ao inicializar configurações do banco de dados.", e);
        }
    }

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            throw new RuntimeException("Falha ao conectar ao banco de dados.", e);
        }
    }
}
