package controller;

import bean.Aluno;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Model;

public class Controller extends HttpServlet {

    //var para o ambiente
    int ra;
    String nome;
    String curso;
    Aluno aluno = new Aluno();
    List<Aluno> alunosDados; // armazena "todos" os alunos recuperados pelo Model
    List<Aluno> alunoDados; // armazena apenas os dados de "um" Aluno

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // configuração para manter a acentuação e caracteres especiais
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        //conexão com a camada do Model (Banco de Dados)
        try {
            //chamar o Model
            Model alunoModel = new Model();

            // atribuindo os valores retornados do Model para uma variável
            alunosDados = alunoModel.listar();

            request.setAttribute("listaAlunos", alunosDados);
            request.getRequestDispatcher("view_listar.jsp").
                    forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("mensagem", e.getMessage());
            request.getRequestDispatcher("view_mensagem.jsp").
                    forward(request, response);
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // essas linhas configuram o código de página 
        // ou seja, acentos e caracteres especiais
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // vamos criar o controle para administrar o que está acontecendo
        // vamos criar uma variável para recuperar qual "opção" o usuário escolheu
        // esta opção virá dos formulários, conforme a linha abaixo:
        // <input type="hidden" name="operacao" value="tipo_da_operacao" />
        String operacao = request.getParameter("operacao");

        // criaremos as opções de operações que vamos usar nesta aplicação
        // Inserir, Pesquisar, Editar, Atualizar, Excluir e Confirmar Exclusão
        // vamos usar o switch para selecionar a opção desejada
        // todas as operações abaixo são referentes a banco de dados
        switch (operacao) {
            case "Inserir":
                try {
                    //passar os valores recebidos do form para o objeto
                    aluno.setRa(request.getParameter("ra"));
                    aluno.setNome(request.getParameter("nome"));
                    aluno.setCurso(request.getParameter("curso"));

                    //chamar o model pra fazer a operação
                    Model alunoModel = new Model();

                    //chamar o método de inclusão, passando o objeto (aluno)
                    alunoModel.inserir(aluno);

                    request.setAttribute("mensagem", alunoModel.toString());

                } catch (SQLException ex) {
                    request.setAttribute("mensagem", ex.getMessage());

                }
                // redireciona para a view_mensagem
                request.getRequestDispatcher("view_mensagem.jsp").
                        forward(request, response);
                break;

            case "Pesquisar":
                String valorDigitado = request.getParameter("valor");

                try {
                    Model alunoModel = new Model();

                    //verificar quem está chegando (tipo da pesquisa)
                    switch (request.getParameter("tipo")) {
                        case "ra":
                            aluno.setRa(valorDigitado);
                            break;

                        case "nome":
                            aluno.setNome(valorDigitado);
                            break;

                        case "curso":
                            aluno.setCurso(valorDigitado);
                            break;
                    }
                    alunosDados = alunoModel.pesquisar(aluno,
                            request.getParameter("tipo"));

                    //tratar os dados
                    if (alunosDados.isEmpty()) { // isEmpty() é vázio
                        request.setAttribute("mensagem", "Não localizado");
                        request.getRequestDispatcher("view_mensagem.jsp").
                                forward(request, response);
                    } else {
                        request.setAttribute("listaAlunos", alunosDados);
                        request.getRequestDispatcher("view_listar.jsp").
                                forward(request, response);
                    }

                } catch (SQLException e) {
                    request.setAttribute("mensagem", e.getMessage());
                    request.getRequestDispatcher("view_mensagem.jsp").
                            forward(request, response);
                }
                break;

            case "Editar":
                try {
                    Model alunoModel = new Model();
                    aluno.setRa(request.getParameter("ra"));
                    alunoDados = alunoModel.pesquisar(aluno, "ra");

                    request.setAttribute("alunoDados", alunoDados);
                    request.getRequestDispatcher("view_editar.jsp").
                            forward(request, response);
                } catch (SQLException e) {
                    request.setAttribute("mensagem", e.getMessage());
                    request.getRequestDispatcher("view_mensagem.jsp").
                            forward(request, response);
                }
                break;

            case "Excluir":
                try {
                    // passar os valores recebidos do formulário para o objeto
                    aluno.setRa(request.getParameter("ra"));

                    // chama o model para fazer a operação
                    Model alunoModel = new Model();

                    // chamar o método de inclusão, passando o objeto "aluno"
                    alunoModel.excluir(aluno);

                    // composição da mensagem de retorno
                    request.setAttribute("mensagem", alunoModel.toString());

                } catch (SQLException ex) {
                    // composição da mensagem de retorno
                    request.setAttribute("mensagem", ex.getMessage());
                }
                // redireciona para a view_mensagem
                request.getRequestDispatcher("view_mensagem.jsp").
                        forward(request, response);

            case "Confirmar Exclusao":
                request.setAttribute("mensagem",
                        "Você clicou em Confirmar Exclusão");
                break;
        }
        // redireciona para a view_mensagem
        request.getRequestDispatcher("view_mensagem.jsp").
                forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
