package com.budgetbakers.servlets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 *  GoogleSignInServlet page.
 */

@WebServlet("/GoogleSignInServlet")
public class GoogleSignInServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Replace with your actual CLIENT_ID from Google Cloud
    private static final String CLIENT_ID = "264389169162-dgq1nvs9lofsg6tlekdvotvtkcctgl3q.apps.googleusercontent.com";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // ✅ Read token from redirect query parameter
        String idTokenString = request.getParameter("credential");

        // Null check
        if (idTokenString == null || idTokenString.isEmpty()) {
            response.getWriter().println("ID token is missing from request.");
            return;
        }

        try {
            // Create Google ID token verifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            // Verify token
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Get user details
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Logging for debugging
                System.out.println("Google login successful: " + email);

                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("email", email);
                session.setAttribute("name", name);

                // Redirect to home page
                //response.sendRedirect("records.jsp");
                response.sendRedirect("ViewRecordsServlet");
            } else {
                response.getWriter().println("Invalid ID token.");
            }

        } catch (GeneralSecurityException e) {
            throw new ServletException("Security exception: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ServletException("Error verifying token: " + e.getMessage(), e);
        }
    }

    // Optional: if Google ever sends POST, forward it to doGet
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
