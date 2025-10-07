package com.budgetbakers.servlets;

import com.budgetbakers.service.UserService;
import com.budgetbakers.entity.Accounts;
import com.budgetbakers.entity.User; // assuming you have a User entity

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 *  AccountsServlet page.
 */

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {

    private UserService userService = new UserService(); // reuse single instance

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email"); // logged-in user email

        // Fetch all accounts for the logged-in user
        List<Accounts> accounts = userService.getUserAccounts(email);
        request.setAttribute("accounts", accounts);

        // Forward to JSP
        request.getRequestDispatcher("accounts.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email"); // logged-in user email

        // Fetch the logged-in user's ID
        //int userId = userService.getUserIdByEmail(email);
        int userId = userService.getOrCreateUserId(email);

        System.out.println("Session email: '" + email + "'");
        if (userId == 0) {
            session.setAttribute("message", "Invalid user! Cannot perform action.");
            response.sendRedirect(request.getContextPath() + "/accounts");
            return;
        }

        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "add":
                	//int user_Id =  Integer.parseInt(request.getParameter("user_Id"));
                    String name = request.getParameter("name");
                    String type = request.getParameter("type");
                    double balance = Double.parseDouble(request.getParameter("balance"));
                    String currency = request.getParameter("currency");
                    if (currency == null || currency.isEmpty()) currency = "INR"; 
                   // String email = (String) session.getAttribute("email"); 
                    //String email = (String) session.getAttribute("email");
                    boolean added = userService.addAccount(userId, name, type, balance,currency,email);
                    session.setAttribute("message", added ? "Account added successfully!" : "Failed to add account.");
                    break;

                case "edit":
                    int editId = Integer.parseInt(request.getParameter("id"));
                    String editName = request.getParameter("name");
                    String editType = request.getParameter("type");
                    double editBalance = Double.parseDouble(request.getParameter("balance"));
                    boolean updated = userService.updateAccount(editId, editName, editType, editBalance);
                    session.setAttribute("message", updated ? "Account updated successfully!" : "Failed to update account.");
                    break;

                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    boolean deleted = userService.deleteAccount(deleteId);
                    session.setAttribute("message", deleted ? "Account deleted successfully!" : "Failed to delete account.");
                    break;

                default:
                    session.setAttribute("message", "Invalid action!");
                    break;
            }
        }

        response.sendRedirect(request.getContextPath() + "/accounts");
    }

}
