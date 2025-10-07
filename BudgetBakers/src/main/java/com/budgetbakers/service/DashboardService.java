package com.budgetbakers.service;

import java.sql.*;
import java.util.*;

import com.budgetbakers.entity.Records;
import com.budgetbakers.utils.DbConnector;

/**
 *  DashboardService page.
 */

public class DashboardService {

    // Get total income for current month for all accounts
    public static double getTotalIncome(String email) {
        String sql = "SELECT SUM(amount) as total FROM records WHERE email=? AND record_type='income' AND MONTH(date)=MONTH(CURRENT_DATE()) AND YEAR(date)=YEAR(CURRENT_DATE())";
        return getSingleDoubleValue(email, sql);
    }

    // Get total expense for current month for all accounts
    public static double getTotalExpense(String email) {
        String sql = "SELECT SUM(amount) as total FROM records WHERE email=? AND record_type='expense' AND MONTH(date)=MONTH(CURRENT_DATE()) AND YEAR(date)=YEAR(CURRENT_DATE())";
        return getSingleDoubleValue(email, sql);
    }

    // Helper to get single double value from SQL
    private static double getSingleDoubleValue(String email, String sql) {
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Category-wise income for current month
    public static Map<String, Double> getCategoryWiseIncome(String email) {
        String sql = "SELECT category, SUM(amount) as total FROM records " +
                     "WHERE email=? AND record_type='income' AND MONTH(date)=MONTH(CURRENT_DATE()) AND YEAR(date)=YEAR(CURRENT_DATE()) " +
                     "GROUP BY category";
        return getCategoryWiseMap(email, sql);
    }

    // Category-wise expense for current month
    public static Map<String, Double> getCategoryWiseExpense(String email) {
        String sql = "SELECT category, SUM(amount) as total FROM records " +
                     "WHERE email=? AND record_type='expense' AND MONTH(date)=MONTH(CURRENT_DATE()) AND YEAR(date)=YEAR(CURRENT_DATE()) " +
                     "GROUP BY category";
        return getCategoryWiseMap(email, sql);
    }

    // Helper to get category-wise map
    private static Map<String, Double> getCategoryWiseMap(String email, String sql) {
        Map<String, Double> map = new LinkedHashMap<>(); // preserve order
        try (Connection conn = DbConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("category"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}

