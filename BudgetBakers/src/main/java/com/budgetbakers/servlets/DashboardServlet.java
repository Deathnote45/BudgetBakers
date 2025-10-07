package com.budgetbakers.servlets;

import com.budgetbakers.service.DashboardService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

/**
 *  DashboardServlet page.
 */
@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = (String) request.getSession().getAttribute("email");
        if (email == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get totals
        double totalIncome = DashboardService.getTotalIncome(email);
        double totalExpense = DashboardService.getTotalExpense(email);
        double netAmount = totalIncome - totalExpense;

        // Category-wise breakdown
        Map<String, Double> categoryIncome = DashboardService.getCategoryWiseIncome(email);
        Map<String, Double> categoryExpense = DashboardService.getCategoryWiseExpense(email);
        

        // Set attributes for JSP
        request.setAttribute("totalIncome", totalIncome);
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("netAmount", netAmount);
        request.setAttribute("categoryIncome", categoryIncome);
        request.setAttribute("categoryExpense", categoryExpense);
        
        

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
