// Revisão de código-fonte com erros e comentários evidenciando os problemas

package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    // Método 1: Conexão com o banco de dados (erro: não fecha a conexão e ignora exceções)
    public Connection conectarBD() {
        Connection conn = null;
        try(
            Class.forName("com.mysql.Driver.Manager").newInstance();
            String url = "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";
            conn = DriverManager.getConnection(url);
        )catch (Exception e) { }
        return conn;}
    // Variável para armazenar o nome, inicializada vazia (problema de convenção: usar public e não final)
    public String nome="";
    // Variável para armazenar o resultado da verificação
    public boolean result = false;
    // Método 2: Verificação de Usuário (erro: injeção de SQL e vazamento de recursos)
    public boolean verificarUsuario(String login, String senha) {
        String sql = "";
        Connection conn = conectarBD();
        //INSTRUÇÃO SQL
        sql += "select nome from usuarios ";
        sql +="where login = " + "'" + login + "'";
        sql += " and senha = " + "'" + senha + "';";
        try(
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                result = true;
                nome = rs.getString("nome");}
        )catch (Exception e) { }
        return result; }
        // erro: recursos não são fechados (conn, st, rs)
    }//fim da class