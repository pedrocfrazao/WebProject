/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
@WebServlet(name = "SignUpServlet", urlPatterns = {"/SignUpServlet"})
public class SignUpServlet extends HttpServlet {

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
                     
            String name = request.getParameter("name");
            String dateOfBirth = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String ddd = request.getParameter("ddd");
            String number = request.getParameter("number");
            String provider = request.getParameter("provider");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String passwordConfirmation = request.getParameter("passwordConfirmation");
            
            if (name.length() > 0 && dateOfBirth.length() > 0 && gender.length() > 0 
                    && ddd.length() > 0 && number.length() > 0 && provider.length() > 0 &&
                    email.length() > 0 && password.length() > 0 && passwordConfirmation.length() > 0) {
                
                Class.forName("org.postgresql.Driver");
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/toppingup", "postgres", "postgres");
                Statement statement = connection.createStatement();
                
                String sqlSelect = "SELECT * from users WHERE email = '"
                        + email + "'";
                
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                
                if (resultSet.next() == true) {
                    
                    // User already exists
                    resultSet.close();
                    
                    response.sendRedirect("/ToppingUp/SignUp.html");
                    
                } else {
                
                    if (password.equals(passwordConfirmation)) {

                        String sqlInsert = "INSERT into users(name, date_of_birth, gender, email, password, balance, is_admin) values ("
                                + "'" + name + "', "
                                + "'" + dateOfBirth + "', "
                                + "'" + gender + "', "
                                + "'" + email + "', "
                                + "'" + password + "', "
                                + "'0.0', "
                                + "'false'"
                                + ");";

                        statement.execute(sqlInsert);
  
                        sqlSelect = "SELECT * from users WHERE email = '"
                                + email + "'";
                        
                        resultSet = statement.executeQuery(sqlSelect);
                        
                        if (resultSet.next() == true) {
                        
                            sqlInsert = "INSERT into cellphones(user_id, ddd, number, provider) values ("
                                    + "'" + resultSet.getInt("id") + "', "
                                    + "'" + ddd + "', "
                                    + "'" + number + "', "
                                    + "'" + provider + "'"
                                    + ");";
                            
                            statement.execute(sqlInsert);
                            resultSet.close();
                                    
                        }
                        
                        statement.close();
                        connection.close();

                    } else {

                        // Password and password confirmation does not match
                        response.sendRedirect("/ToppingUp/SignUp.html");

                    }
                    
                }
                
            } else {
                
                // Blank fields
                response.sendRedirect("/ToppingUp/SignUp.html");
                
            }
            
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet SignUpServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet SignUpServlet at " + request.getContextPath() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
            
        }   catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignUpServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
