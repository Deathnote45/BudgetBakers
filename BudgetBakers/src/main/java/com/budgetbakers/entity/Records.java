package com.budgetbakers.entity;

/**
 * records page.
 */
public class Records {
    private int id;
    private String account;
    private String date;
    private String category;
    private double amount;
    private String description;
    private String recordType; // "income" or "expense"
    
    /**
     * records entity page.
     */
    // Default constructor
    public Records() {}

    // Constructor with all fields
    public Records(int id, String account, String date, String category, double amount, String description, String recordType) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.recordType = recordType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @Override
    public String toString() {
        return "Records{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", recordType='" + recordType + '\'' +
                '}';
    }
}
