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
    
    // Esse método vai servir para: pesquisar, editar e excluir
    public List<Aluno> pesquisar(Aluno aluno,  String tipo){
        List<Aluno> alunos = new ArrayList();
        
        PreparedStatement ps = null;
        String sql = new String();
        
        try {
            // 1 - montar as expressões SQL
            switch(tipo) {
                case "ra":
                    sql = "SELECT * FROM aluno WHERE ra = ?;";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, aluno.getRa());
                    break;
                
                case "nome":
                    sql = "SELECT * FROM aluno WHERE nome = ?;";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, aluno.getNome());
                    break;
                
                case "curso":
                    sql = "SELECT * FROM aluno WHERE curso = ? ORDER BY curso, nome ASC;";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, aluno.getCurso());
                    break;
            }
            
            // 2 - ResultSet (pegar os resultados do BD)
            ResultSet rs = ps.executeQuery();
            
            // 3 - Montar o while (colocar os dados recebidos no objeto (Aluno)
            while(rs.next()){
                aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setRa(rs.getString("ra"));
                aluno.setNome(rs.getString("nome"));
                aluno.setCurso(rs.getString("curso"));
                
                // 4 - Adicionar na lista
                alunos.add(aluno);
            }
            rs.close();
            ps.close();
            
            // 5 - Retornar a lista de alunos
            return alunos;
            
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao pesquisar" + e.getMessage());
        }
    }
    
    //método p/ listar todos os registros (Menu Listar)
        public List<Aluno> listar() {
        // variável para receber a lista de alunos (registros)
        List<Aluno> alunos = new ArrayList();
        try {
            String sql = "SELECT * FROM aluno ORDER BY nome ASC;";
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
        
    // método para inserir registro no BD
    public void inserir(Aluno aluno){
        try {
            String sql = "INSERT INTO aluno (ra, nome, curso) VALUES (?,?,?);";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                //atribuir os valores do objeto (aluno) ao (ps)
                ps.setString(1, aluno.getRa());
                ps.setString(2, aluno.getNome());
                ps.setString(3, aluno.getCurso());
                
                //executar a inclusão
                ps.execute();
                ps.close();
            }
            connection.close();
            this.statusMessage = "Incluido com sucesso!";
            
        } catch (SQLException ex) {
            this.statusMessage = "Falha ao inserir: " + ex.getMessage();
        }
    }
    
    // método para excluir um registro no BD
    public void excluir(Aluno aluno) {
        try {
            String sql = "DELETE FROM aluno WHERE ra = ?;";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, aluno.getRa());

                // executar a inclusão
                ps.execute();
                ps.close();
            }
            connection.close(); // fecha a conexão com o banco de dados
            this.statusMessage = "Excluído com sucesso";
        } catch (SQLException ex) {
            this.statusMessage = "Falha ao excluir: " + ex.getMessage();
        }
    
    }
    
    @Override
    public String toString(){
        return this.statusMessage;
    }
}
