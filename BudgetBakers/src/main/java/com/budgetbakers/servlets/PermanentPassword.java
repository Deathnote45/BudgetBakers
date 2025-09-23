package com.budgetbakers.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetbakers.service.UserService;

/**
 * Servlet implementation class PermanentPassword
 */
@WebServlet("/PermanentPassword")
public class PermanentPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PermanentPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String password=request.getParameter("tempPassword");
//		String email=(String) request.getSession().getAttribute("email");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		System.out.println("login");

		try {
			boolean isCorrect = UserService.validatePermanentPassword(email,password);
			if(isCorrect){
				//System.out.println(password);
				 request.getSession().setAttribute("email", email);
				response.sendRedirect("home.jsp");
				
				System.out.println("login successful");
			}else {
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}

}
