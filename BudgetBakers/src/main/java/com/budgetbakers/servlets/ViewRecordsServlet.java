package com.budgetbakers.servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetbakers.entity.Records;
import com.budgetbakers.service.UserService;

/**
 *  ViewRecordsServlet page.
 */


@WebServlet("/ViewRecordsServlet")
public class ViewRecordsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = (String) request.getSession().getAttribute("email");
        System.out.println("hello");        
        if (email == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        //String email = (String) request.getSession().getAttribute("email");
        List<Records> records=UserService.getUserRecords(email);
        request.setAttribute("records", records);

        // Call service to fetch records
       // List<Records> recordList = UserService.getUserRecords(email);

        // Attach list to request
        //request.setAttribute("records", recordList);

        // Forward to JSP
        request.getRequestDispatcher("records.jsp").forward(request, response);
    }
}
