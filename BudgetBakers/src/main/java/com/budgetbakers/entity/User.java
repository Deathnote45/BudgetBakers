package com.budgetbakers.entity;

import java.sql.Timestamp;

/**
  users page.
 */

public class User {
    private int id;
    private String email;
    private String password;
    private String tempPassword;
    private boolean isTemp;
    private Timestamp createdAt;
   
    /**
     * users entity page.
     */
    // Constructors
    public User() {}

    public User(String email, String tempPassword) {
        this.email = email;
        this.tempPassword = tempPassword;
        this.isTemp = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
