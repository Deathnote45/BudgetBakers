package com.budgetbakers.servlets;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.budgetbakers.service.UserService;
import com.budgetbakers.utils.MailUtil;
import jakarta.mail.MessagingException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String email = request.getParameter("email");
        //String password = request.getParameter("password");
        HttpSession session = request.getSession();
        //System.out.println(email + password);
        
        try {
            if (userService.isNewUser(email)) {
                // New user flow
                String tempPassword = userService.registerUser(email);
                session.setAttribute("email", email);
                // In a real app, you would send this tempPassword via email
                //System.out.println("Generated temp password for " + email + ": " + tempPassword);
//                String subject = "Your Temporary Password";
//                String body = "Hello,\n\nYour temporary password is: " + tempPassword +
//                              "\nPlease log in and set a permanent password.";
//                try {
//					MailUtil.sendEmail(email, subject, body);
//				} catch (MessagingException e) {
//					System.out.println("email gen failed");
//					e.printStackTrace();
//				}
                response.sendRedirect("tempPassword.jsp?email=" + email);
            } else {
                // Existing user flow
               request.getRequestDispatcher("password.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred with the database.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
}
