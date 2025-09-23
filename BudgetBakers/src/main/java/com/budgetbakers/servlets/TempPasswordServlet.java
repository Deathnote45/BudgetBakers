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

@WebServlet("/TempPasswordServlet")
public class TempPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String tempPassword = request.getParameter("tempPassword");
        HttpSession session = request.getSession();
       

        try {
            if (userService.validateTempPassword(email, tempPassword)) {
                session.setAttribute("email", email);
                response.sendRedirect("setPassword.jsp?email=" + email);
            } else {
                request.setAttribute("errorMessage", "Invalid temporary password. Please try again.");
                request.getRequestDispatcher("tempPassword.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred with the database.");
            request.getRequestDispatcher("tempPassword.jsp").forward(request, response);
        }
    }
}

