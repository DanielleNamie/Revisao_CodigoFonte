// Código corrigido com melhorias de segurança, gerenciamento de recursos e boas práticas

package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Classe responsável por operações de autenticação de usuário.
public class UserCorrigido {
    
    // As variáveis de estado não devem ser públicas e devem ser acessadas via métodos
    private String nome = "";
    private boolean isUserValid = false;
    
    // URL do banco de dados (idealmente lido de um arquivo de configuração)
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver"; // Driver atualizado

    /**
     * Estabelece uma conexão com o banco de dados.
     * @return Connection ativa.
     * @throws SQLException Se a conexão falhar.
     */
    private Connection getConnection() throws SQLException {
        /* Chama Class.forName explicitamente para usar a constante DRIVER_CLASS
        e garantir que o driver JDBC esteja disponível. Se ausente, relança como SQLException. */
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado: " + DRIVER_CLASS, e);
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Verifica se o usuário e senha são válidos utilizando PreparedStatement (Proteção contra SQL Injection).
     * @param login O nome de login do usuário.
     * @param senha A senha do usuário.
     * @return true se o usuário for encontrado e as credenciais forem válidas, false caso contrário.
     */
    public boolean verificarUsuario(String login, String senha) {
        
        // Uso de '?' para evitar SQL Injection
        final String SQL = "SELECT nome FROM usuarios WHERE login = ? AND senha = ?";
        
        // try-with-resources garante que conn, pstmt e rs sejam fechados (vazamento de recursos)
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            // Define os parâmetros da query de forma segura
            pstmt.setString(1, login); // Primeiro '?' é o login
            pstmt.setString(2, senha); // Segundo '?' é a senha
            
            // Executa a query
            try (ResultSet rs = pstmt.executeQuery()) {
                
                // Se houver pelo menos um resultado (o usuário foi encontrado)
                if (rs.next()) {
                    this.isUserValid = true;
                    this.nome = rs.getString("nome");
                } else {
                    this.isUserValid = false; // Garante que é false se não encontrar
                    this.nome = "";
                }
            } // rs fechado automaticamente
            
        } catch (SQLException e) { 
            // Imprime o erro e retorna false
            System.err.println("Erro ao verificar usuário no banco de dados: " + e.getMessage());
            e.printStackTrace();
            this.isUserValid = false;
        }
        
        return this.isUserValid;
    }
    
    public String getNome() {
        return nome;
    }
}
