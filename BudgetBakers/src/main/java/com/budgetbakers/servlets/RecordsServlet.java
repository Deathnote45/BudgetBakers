package com.budgetbakers.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetbakers.entity.Records;
import com.budgetbakers.service.UserService;

/**
 *  RecordsServlet page.
 */

@WebServlet("/RecordsServlet")
public class RecordsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RecordsServlet() {
        super();
    }

	/*
	 * @Override protected void doPost(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * 
	 * // 1. Get parameters from records.jsp form String date =
	 * request.getParameter("date"); String category =
	 * request.getParameter("category"); String amountStr =
	 * request.getParameter("amount"); String description =
	 * request.getParameter("description"); String account =
	 * request.getParameter("account");
	 * 
	 * 
	 * 
	 * 
	 * double amount = 0.0; try { amount = Double.parseDouble(amountStr); } catch
	 * (NumberFormatException e) { e.printStackTrace(); }
	 * 
	 * // 2. Get logged-in user's email from session String email = (String)
	 * request.getSession().getAttribute("email"); //List<Records>
	 * records=UserService.getUserRecords(email); //request.setAttribute("records",
	 * records);
	 * 
	 * // Safety check if user is not logged in if (email == null) {
	 * request.setAttribute("message", "You must be logged in to add records!");
	 * request.getRequestDispatcher("login.jsp").forward(request, response); return;
	 * }
	 * 
	 * // 3. Call UserService method to save the record boolean isInserted =
	 * UserService.addRecord(date, category, amount, description, email,account);
	 * 
	 * // 4. Set feedback message if (isInserted) { request.setAttribute("message",
	 * "Record added successfully!"); } else { request.setAttribute("message",
	 * "Failed to add record!"); }
	 * 
	 * // 5. Forward back to records.jsp to show message
	 * //request.getRequestDispatcher("ViewRecordsServlet").forward(request,
	 * response); response.sendRedirect("ViewRecordsServlet"); }
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Get form parameters
        String date = request.getParameter("date");
        String category = request.getParameter("category");
        String recordType = request.getParameter("record_type"); // new field
        String amountStr = request.getParameter("amount");
        String description = request.getParameter("description");
        String account = request.getParameter("account");

        double amount = 0.0;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String email = (String) request.getSession().getAttribute("email");

        if (email == null) {
            request.setAttribute("message", "You must be logged in to add records!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        boolean isInserted = UserService.addRecord(date, category, amount, description, recordType, email, account);

        if (isInserted) {
            request.getSession().setAttribute("message", "Record added successfully!");
        } else {
            request.getSession().setAttribute("message", "Failed to add record!");
        }

        response.sendRedirect("ViewRecordsServlet");
    }

}
