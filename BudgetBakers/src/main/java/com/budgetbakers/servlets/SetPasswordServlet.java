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

@WebServlet("/SetPasswordServlet")
public class SetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        HttpSession session = request.getSession();
        System.out.println(confirmPassword);

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match or are empty.");
            request.getRequestDispatcher("setPassword.jsp").forward(request, response);
            return;
        }

        try {
            userService.setPassword(email, newPassword);
            session.invalidate(); // Invalidate session to force re-login
            //response.sendRedirect("login.jsp?message=Password+set+successfully.+Please+log+in+again.");
            response.sendRedirect("home.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred with the database.");
            request.getRequestDispatcher("setPassword.jsp").forward(request, response);
        }
    }
}

