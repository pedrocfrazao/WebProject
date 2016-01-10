/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author André
 */
@WebServlet(name = "ForgotLoginServlet", urlPatterns = {"/ForgotLoginServlet"})
public class ForgotLoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            
            response.setContentType("text/html;charset=UTF-8");
            
            String email = request.getParameter("email");
            
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/toppingup", "postgres", "postgres");
            Statement statement = connection.createStatement();
            
            String sqlSelect = "SELECT * from users WHERE email = '"
                    + email + "'";
            
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            
            if (resultSet.next() == true) {
                
                // User exists
                User user = new User(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance"),
                        resultSet.getBoolean("is_admin"));
                
                String password = user.getPassword();
                
                System.out.println("Password: " + password + " sent, check you inbox.");
            
            } else {
                
                // User does not exists
                response.sendRedirect("/ToppingUp/ForgotLogin.html");
                
            }
            
            resultSet.close();
            statement.close();
            connection.close();
            
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet ForgotLoginServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet ForgotLoginServlet at " + request.getContextPath() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }   catch (ClassNotFoundException ex) {
            Logger.getLogger(ForgotLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ForgotLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
