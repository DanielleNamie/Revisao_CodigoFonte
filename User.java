// Revisão de código-fonte com erros e comentários evidenciando os problemas

package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    // Método 1: Conexão com o banco de dados (erro: não fecha a conexão e ignora exceções)
    public Connection conectarBD(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.Driver.Manager").newInstance();
            String url = "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";
            conn = DriverManager.getConnection(url);
        // ERRO: Sintaxe incorreta do catch - deveria ser 'catch (Exception e)'
        }catch (Exception e) { }
        return conn;}
    
    // ERRO: Variável de instância sem modificador de acesso (deveria ser 'private')
    public String nome="";
    public boolean result = false;
    // Método 2: Verificação de Usuário (erro: injeção de SQL e vazamento de recursos)
    public boolean verificarUsuario(String login, String senha){
        String sql = "";
        Connection conn = conectarBD();
        // ERRO: Comentário incorreto ou código incompleto - "//INSTRUÇÃO SQL"
        //INSTRUÇÃO SQL
        sql += "select nome from usuarios ";
        // ERRO: Concatenação de string vulnerável a SQL Injection
        sql +="where login = " + "'" + login + "';";
        // ERRO: Concatenação de string vulnerável a SQL Injection
        sql += " and senha = " + "'" + senha + "';";
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                result = true;
                nome = rs.getString("nome");}
        // ERRO: Bloco catch vazio - deveria tratar a exceção
        }catch (Exception e) { }
        // ERRO: Falta fechar recursos (Statement, ResultSet, Connection)
        return result;}
    // ERRO: Comentário incompleto ou desnecessário
}//fim da class
