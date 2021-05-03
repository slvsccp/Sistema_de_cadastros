package model;
import bean.Aluno;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author souza
 * Essa classe controla o acesso ao banco de dados
 * Teremos os seguintes métodos:
 * inserir, pesquisar*, editar, atualizar, excluir
 * pesquisar (listar e listar todos)
 */
public class Model implements Serializable{
    private Connection connection = null;
    private String statusMessage;
    
    //construtor
    public Model() throws SQLException{
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    //método p/ listar todos os registros (Menu Listar)
        public List<Aluno> listar() {
        // variável para receber a lista de alunos (registros)
        List<Aluno> alunos = new ArrayList();
        try {
            String sql = "SELECT * FROM alunos ORDER BY nome ASC;";
            try (
                    // preparando a consulta
                    PreparedStatement ps = connection.prepareStatement(sql);
                    // receber o resultado da consulta
                    ResultSet rs = ps.executeQuery()) {
                
                // percorrer os registros recuperados
                while(rs.next()){
                    Aluno aluno = new Aluno();
                    aluno.setId(rs.getInt("id"));
                    aluno.setRa(rs.getString("ra"));
                    aluno.setNome(rs.getString("nome"));
                    aluno.setCurso(rs.getString("curso"));
                    
                    //coloca dados dentro da lista (alunos)
                    alunos.add(aluno);
                }
                rs.close(); //fecha consulta no BD
                ps.close(); //fecha conexao BD
            }
            //retorna lista de objetos (alunos)
            return alunos;
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao listar.");
        }
    }
}
