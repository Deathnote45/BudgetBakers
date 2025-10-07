package com.budgetbakers.entity;

/**
 * accounts page.
 */
public class Accounts {
    private int id;
    private String name;
    private String type;        
    private double balance;
    private boolean archived;
    private String currency;  
    
    /**
     * accounts entity page.
     */
    // Constructors
    public Accounts() {}
    public Accounts(int id, String name, String type, double balance, boolean archived, String currency) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.archived = archived;
        this.currency = currency;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isArchived() {
        return archived;
    }
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
