package com.budgetbakers.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *  DbConnector page.
 */

public class DbConnector {

	private static DbConnector instance;
	private Properties props;

	private DbConnector() {
		props = new Properties();
		try (InputStream input = DbConnector.class.getClassLoader().getResourceAsStream("Db.properties")) {
			if (input == null) {
				System.err.println("Error: Db.properties file not found. Ensure it is in the classpath, e.g., in src/main/resources.");
				return;
			}
			props.load(input);
		} catch (Exception e) {
			System.err.println("Error loading Db.properties: " + e.getMessage());
		}
	}

	public static synchronized DbConnector getInstance() {
		if (instance == null) {
			instance = new DbConnector();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		String url = props.getProperty("Db.url");
		String username = props.getProperty("Db.username");
		String password = props.getProperty("Db.password");
		
		if (url == null || url.isEmpty()) {
            throw new SQLException("Database URL is not set in Db.properties.");
        }
		
		System.out.println("Attempting to connect with URL: " + url);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
			throw new SQLException("MySQL JDBC Driver not found.", e);
		}
	}
}
