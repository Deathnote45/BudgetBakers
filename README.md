# 💰 BudgetBakers — Smart Personal Finance Tracker

BudgetBakers is a full-stack **personal finance management system** that helps users efficiently track income, expenses, transfers, and account balances.  
Built with **Java (Servlets, JSP, JDBC, MySQL)**, it provides real-time insights through interactive dashboards and data visualizations.

---

## 🚀 Features

✅ **User Authentication**
- Secure signup and login system (passwords hashed with BCrypt)

✅ **Account Management**
- Add, update, and delete multiple financial accounts (e.g., cash, bank, card)
- Track account balances dynamically

✅ **Transaction Management**
- Add income, expense, and transfer transactions  
- Categorize transactions (e.g., Food, Travel, Bills, etc.)  
- Notes and timestamps for better tracking  

✅ **Dashboard & Analytics**
- Real-time balance overview  
- Current month cash flow, income, and spending  
- Interactive charts powered by Chart.js

✅ **Email Integration**
- Optional email notifications using JavaMail API

✅ **Responsive UI**
- Clean, minimal JSP interface for smooth navigation

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-------------|
| Frontend | HTML, CSS, JSP, JSTL |
| Backend | Java Servlets, JDBC |
| Database | MySQL |
| Security | BCrypt (password hashing) |
| Charts | Chart.js |
| Build Tool | Maven / Manual Compilation |
| Server | Apache Tomcat |

---

## 🧩 Database Schema Overview

### 🗂 Tables:
- `users` — Stores user credentials and profile details  
- `accounts` — User accounts (cash, card, bank, etc.)  
- `categories` — Expense/income categories  
- `transactions` — All financial transactions  

**Example — `transactions` table:**
```sql
CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  account_id INT NOT NULL,
  category_id INT DEFAULT NULL,
  transaction_type ENUM('Expense', 'Income', 'Transfer') NOT NULL,
  amount DECIMAL(15,2) NOT NULL,
  transaction_date DATETIME NOT NULL,
  note TEXT,
  to_account_id INT DEFAULT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
  FOREIGN KEY (to_account_id) REFERENCES accounts(id) ON DELETE SET NULL,
  FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);
