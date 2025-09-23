package com.budgetbakers.service;

import com.budgetbakers.utils.DbConnector;
import com.budgetbakers.utils.MailUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserService {

    // Check if user exists
//    public boolean isNewUser(String email) throws SQLException {
//        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
//        try (Connection conn = DbConnector.getInstance().getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, email);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt(1) == 0;
//                }
//            }
//        }
//        return true;
//    }
	public boolean isNewUser(String email) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
	    try (Connection conn = DbConnector.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, email);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                // If count is 0, user is new; otherwise, user exists
	                return count == 0;
	            }
	        }
	    }
	    // If somehow query fails or no row returned, assume user exists
	    return false;
	}


    // Register a new user with a temporary password (plain text) and send email
    public String registerUser(String email) throws SQLException {
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // Store temp password as it is
        String sql = "INSERT INTO users (email, temp_password, is_temp) VALUES (?, ?, TRUE)";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, tempPassword); // plain text
            pstmt.executeUpdate();
        }

        // Send temp password via email
        try {
            String subject = "Your Temporary Password";
            String body = "Hello,\n\nYour temporary password is: " + tempPassword +
                          "\nPlease log in and set a permanent password.";
            MailUtil.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace(); // handle email errors
        }

        return tempPassword;
    }

    // Validate temporary password
    public boolean validateTempPassword(String email, String tempPassword) throws SQLException {
        String sql = "SELECT is_temp, temp_password FROM users WHERE email = ?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean isTemp = rs.getBoolean("is_temp");
                    String storedTempPassword = rs.getString("temp_password");
                    return isTemp && tempPassword.equals(storedTempPassword); // plain text check
                }
            }
        }
        return false;
    }

    // Validate permanent password (hashed)
    public static boolean validatePermanentPassword(String email, String password) throws SQLException {
        String sql = "SELECT password, is_temp FROM users WHERE email = ?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean isTemp = rs.getBoolean("is_temp");
                    String storedPassword = rs.getString("password");
                    System.out.println("password validation check");
                    System.out.println(isTemp);
                    System.out.println(BCrypt.checkpw(password, storedPassword));
                    return !isTemp && BCrypt.checkpw(password, storedPassword); // hashed check
                }
            }
        }
        return false;
    }

    // Set permanent password (hashed)
    public void setPassword(String email, String newPassword) throws SQLException {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        String sql = "UPDATE users SET password = ?, is_temp = FALSE, temp_password = NULL WHERE email = ?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        }
    }
}
