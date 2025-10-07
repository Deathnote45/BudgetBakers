package com.budgetbakers.service;

import com.budgetbakers.utils.DbConnector;
import com.budgetbakers.utils.MailUtil;
import com.budgetbakers.entity.Records;
import com.budgetbakers.entity.Accounts;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

/**
 *  UserService page.
 */

public class UserService {

    // ------------------ User Methods ------------------

    public boolean isNewUser(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }

    public String registerUser(String email) throws SQLException {
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String sql = "INSERT INTO users (email, temp_password, is_temp) VALUES (?, ?, TRUE)";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            pstmt.setString(2, tempPassword);
            pstmt.executeUpdate();
        }

        try {
            String subject = "Your Temporary Password";
            String body = "Hello,\n\nYour temporary password is: " + tempPassword +
                    "\nPlease log in and set a permanent password.";
            MailUtil.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempPassword;
    }

    public boolean validateTempPassword(String email, String tempPassword) throws SQLException {
        String sql = "SELECT is_temp, temp_password FROM users WHERE email = ?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_temp") && tempPassword.equals(rs.getString("temp_password"));
                }
            }
        }
        return false;
    }

    public static boolean validatePermanentPassword(String email, String password) throws SQLException {
        String sql = "SELECT password, is_temp FROM users WHERE email = ?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean isTemp = rs.getBoolean("is_temp");
                    String storedPassword = rs.getString("password");
                    return !isTemp && BCrypt.checkpw(password, storedPassword);
                }
            }
        }
        return false;
    }

    public void setPassword(String email, String newPassword) throws SQLException {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE users SET password=?, is_temp=FALSE, temp_password=NULL WHERE email=?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, email.trim());
            pstmt.executeUpdate();
        }
    }

    public int getUserIdByEmail(String email) {
        int userId = 0;
        String sql = "SELECT id FROM users WHERE email=?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public int getOrCreateUserId(String email) {
        int userId = getUserIdByEmail(email.trim());

        if (userId == 0) {
            String sql = "INSERT INTO users (email) VALUES (?)";
            try (Connection conn = DbConnector.getInstance().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, email.trim());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            userId = rs.getInt(1);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userId;
    }

    // ------------------ Accounts Methods ------------------

    public boolean addAccount(int userId, String name, String type, double balance, String currency, String email) {
        boolean inserted = false;
        String sql = "INSERT INTO accounts (user_id, name, account_type, initial_balance, currency, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setDouble(4, balance);
            pstmt.setString(5, currency);
            pstmt.setString(6, email);

            inserted = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inserted;
    }

    public List<Accounts> getUserAccounts(String email) {
        List<Accounts> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id=(SELECT id FROM users WHERE email=?) ORDER BY id ASC";

        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Accounts acc = new Accounts();
                    acc.setId(rs.getInt("id"));
                    acc.setName(rs.getString("name"));
                    acc.setType(rs.getString("account_type"));
                    acc.setBalance(rs.getDouble("initial_balance"));
                    acc.setCurrency(rs.getString("currency"));
                    accounts.add(acc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean updateAccount(int id, String name, String type, double balance) {
        boolean updated = false;
        String sql = "UPDATE accounts SET name=?, account_type=?, initial_balance=? WHERE id=?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, balance);
            pstmt.setInt(4, id);

            updated = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public boolean deleteAccount(int id) {
        boolean deleted = false;
        String sql = "DELETE FROM accounts WHERE id=?";
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            deleted = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    // ------------------ Records Methods ------------------

	/*
	 * public static boolean addRecord(String date, String category, double amount,
	 * String description, String email, String account) { boolean isInserted =
	 * false; String sql =
	 * "INSERT INTO records (account, date, category, amount, description, email) VALUES (?,?,?,?,?,?)"
	 * ;
	 * 
	 * try (Connection conn = DbConnector.getInstance().getConnection();
	 * PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 * 
	 * pstmt.setString(1, account); pstmt.setString(2, date); pstmt.setString(3,
	 * category); pstmt.setDouble(4, amount); pstmt.setString(5, description);
	 * pstmt.setString(6, email.trim());
	 * 
	 * isInserted = pstmt.executeUpdate() > 0; } catch (SQLException e) {
	 * e.printStackTrace(); } return isInserted; }
	 */
    
    public static boolean addRecord(String date, String category, double amount, String description,
            String recordType, String email, String account) {
String sql = "INSERT INTO records(date, category, amount, description, record_type, email, account) "
+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
try (Connection conn = DbConnector.getInstance().getConnection();
PreparedStatement ps = conn.prepareStatement(sql)) {

ps.setString(1, date);
ps.setString(2, category);
ps.setDouble(3, amount);
ps.setString(4, description);
ps.setString(5, recordType);
ps.setString(6, email);
ps.setString(7, account);

return ps.executeUpdate() > 0;
} catch (SQLException e) {
e.printStackTrace();
return false;
}
}


/*
 * public static List<Records> getUserRecords(String email) { List<Records>
 * records = new ArrayList<>(); String sql =
 * "SELECT * FROM records WHERE email=? ORDER BY date DESC";
 * 
 * try (Connection conn = DbConnector.getInstance().getConnection();
 * PreparedStatement pstmt = conn.prepareStatement(sql)) {
 * 
 * pstmt.setString(1, email.trim()); try (ResultSet rs = pstmt.executeQuery()) {
 * while (rs.next()) { Records record = new Records();
 * record.setId(rs.getInt("id")); record.setAccount(rs.getString("account"));
 * record.setDate(rs.getString("date"));
 * record.setCategory(rs.getString("category"));
 * record.setAmount(rs.getDouble("amount"));
 * record.setDescription(rs.getString("description")); records.add(record); } }
 * } catch (SQLException e) { e.printStackTrace(); } return records; }
 */
    public static List<Records> getUserRecords(String email) {
        List<Records> records = new ArrayList<>();
        String sql = "SELECT * FROM records WHERE email=? ORDER BY date DESC";

        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Records record = new Records();
                    record.setId(rs.getInt("id"));
                    record.setAccount(rs.getString("account"));
                    record.setDate(rs.getString("date"));
                    record.setCategory(rs.getString("category"));
                    record.setAmount(rs.getDouble("amount"));
                    record.setDescription(rs.getString("description"));
                    record.setRecordType(rs.getString("record_type")); // NEW: set record type
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }


    public static Map<String, Double> getCategoryWiseExpenses(String email) {
        Map<String, Double> categoryExpenses = new LinkedHashMap<>();
        String sql = "SELECT category, SUM(ABS(amount)) AS total " +
                     "FROM records " +
                     "WHERE email = ? " +
                     "GROUP BY category " +
                     "ORDER BY total DESC";

        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categoryExpenses.put(rs.getString("category"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryExpenses;
    }

  
}
