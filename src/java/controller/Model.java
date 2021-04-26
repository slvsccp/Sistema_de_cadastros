package controller;

import java.io.Serializable;
import java.sql.Connection;

/**
 *
 * @author matheus
 * Classe controla o acesso ao BD.
 * Métodos: inserir, pesquisar*, editar, atualizar, excluir
 * pesquisar (listar e listar todos)
 */
public class Model implements Serializable{
    
    //Conexão com o BD
    private Connection connection = null;
    //Estado de uma operação
    private String statusMessage;
    
    
    public void insert(Object aluno) {
        this.statusMessage = "Inserido com sucesso";
    }
            
}
