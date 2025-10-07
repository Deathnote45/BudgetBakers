<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Wallet UI</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      background: #f5f5f5;
    }

    /* Navbar */
    .navbar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: #fff;
      padding: 10px 20px;
      border-bottom: 1px solid #ccc;
    }

    .navbar-left {
      display: flex;
      align-items: center;
      gap: 20px;
    }

    .navbar-left img {
      height: 40px;
    }

    .nav-links a {
      text-decoration: none;
      color: black;
      margin: 0 10px;
      font-weight: 500;
    }

    .avatar-container {
      position: relative;
      display: inline-block;
    }

    .avatar-btn {
      background: none;
      border: none;
      cursor: pointer;
    }

    .avatar-btn img {
      width: 35px;
      height: 35px;
      border-radius: 50%;
    }

    /* Dropdown */
    .dropdown {
      display: none;
      position: absolute;
      right: 0;
      top: 45px;
      background: #fff;
      box-shadow: 0 2px 6px rgba(0,0,0,0.15);
      border-radius: 5px;
      min-width: 150px;
      z-index: 100;
    }

    .dropdown p, .dropdown a {
      padding: 10px;
      margin: 0;
      display: block;
      color: #333;
      text-decoration: none;
    }

    .dropdown a:hover {
      background: #f0f0f0;
    }

    /* Layout */
    .container {
      display: flex;
      margin: 20px;
    }

    /* Left Panel */
    .sidebar {
      background: #fff;
      border: 1px solid #ccc;
      padding: 20px;
      width: 200px;
    }

    .sidebar h3 {
      margin-top: 0;
      color: #4a772f;
    }

    .sidebar label {
      display: block;
      margin: 10px 0 5px;
    }

    .sidebar input {
      width: 100%;
      padding: 5px;
      border: 1px solid #000;
      border-radius: 3px;
    }

    /* Records Section */
    .records {
      flex: 1;
      padding: 20px;
      background: #fff;
      border: 1px solid #ccc;
      margin-left: 20px;
    }

    .record {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 15px;
      border-radius: 12px;
      margin: 10px 0;
      color: white;
      font-weight: bold;
    }

    .record .date, .record .category {
      background: rgba(255,255,255,0.5);
      padding: 8px 12px;
      border-radius: 8px;
      color: black;
      font-size: 14px;
    }

    .record.income {
      background: linear-gradient(to bottom, #6aa7f8, #2667c4);
    }

    .record.expense {
      background: linear-gradient(to bottom, #f88787, #c42c2c);
    }

    /* Add Record Button */
    .add-btn {
      display: inline-block;
      margin-top: 20px;
      padding: 10px 20px;
      background: #a3d27c;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      font-weight: bold;
    }

    .add-btn:hover {
      background: #8bc34a;
    }

    .message {
      color: green;
      font-weight: bold;
      margin-bottom: 15px;
    }
  </style>
  <style>
.records-container {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    margin: 20px 0;
}

.record-card {
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 3px 6px rgba(0,0,0,0.1);
    padding: 22px;
    width: 1000px;
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    transition: transform 0.2s;
    font-size: 50px;
    font-weight: bold;
    
}
.stylecard {
       padding: 20px;
       
}

.record-card:hover {
    transform: translateY(-5px);
}

.record-card.income {
    border-left: 5px solid green;
}

.record-card.expense {
    border-left: 5px solid red;
}

.record-header {
    display: flex;
    justify-content: space-between;
    font-weight: bold;
    font-size: 14px;
    color: #555;
}

.record-body {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.record-category {
    font-size: 16px;
    color: #333;
}

.record-description {
    font-size: 14px;
    color: #777;
}

.record-amount {
    font-weight: bold;
    font-size: 16px;
    text-align: right;
    color: #222;
}
</style>
</head>
<body>

  <!-- Navbar -->
  <div class="navbar">
    <div class="navbar-left">
      <!-- <img src="logo.png" alt="Logo"> -->
      <img src="${pageContext.request.contextPath}/images/logo.svg" alt="BudgetBakers">
      <div class="nav-links">
        <a href="DashboardServlet">Dashboard</a>
       <a href="${pageContext.request.contextPath}/accounts">Accounts</a>
       
        <a href="ViewRecordsServlet">Records</a>
        
      </div>
    </div>

    <div class="avatar-container">
      <button class="avatar-btn" onclick="toggleDropdown()">
        <!-- <img src="avatar.png" alt="shuvangi"> -->
<img src="${pageContext.request.contextPath}/images/avatar.png" alt="Shuvangi">
        
      </button>
      <div class="dropdown" id="dropdownMenu">
        <p><strong>${sessionScope.email}</strong></p>
        <a href="#">Settings</a>
        <a href="logout.jsp">Logout</a>
      </div>
    </div>
  </div>

  <!-- Main Layout -->
  <div class="container">
  <!-- Sidebar Form -->
<div class="sidebar">
  <h3>Add Record</h3>
  <form action="RecordsServlet" method="post">
    <label>Date</label>
    <input type="date" name="date" required>

    <label>Category</label>
    <select name="category" required>
        <option value="Food">Food</option>
        <option value="Transport">Transport</option>
        <option value="Entertainment">Entertainment</option>
        <option value="Bills">Bills</option>
        <option value="Other">Other</option>
    </select>

    <label>Record Type</label>
    <select name="record_type" required>
        <option value="income">Income</option>
        <option value="expense">Expense</option>
    </select>

    <label>Amount</label>
    <input type="number" step="0.01" name="amount" required>

    <label>Description</label>
    <input type="text" name="description">

    <label>Account</label>
    <input type="text" name="account">

    <button type="submit" class="add-btn">+ Add Record</button>
  </form>
</div>
  
  
  
  
  
  
  
  
  
  
  
  
  
  
   <!--  <div class="sidebar">
      <h3>Add Record</h3>
      <form action="RecordsServlet" method="post">
        <label>Date</label>
        <input type="date" name="date" required>

        <label>Category</label>
        <input type="text" name="category" required>

        <label>Amount</label>
        <input type="number" step="0.01" name="amount" required>

        <label>Description</label>
        <input type="text" name="description">
        
         <label>Account</label>
        <input type="text" name="account">
        

        <button type="submit" class="add-btn">+ Add Record</button>
      </form>
    </div> -->
    <c:if test="${not empty message}">
     <c:if test="${not empty message}">
	  </c:if>
    </c:if>
<div
	style="background-color: white; padding: 10px; border: none; border-radius: 5px;">
	<div class="table-responsive"
	style="max-height: 80vh; overflow-y: auto;">
	 
    <!-- Records Section -->
    <div class="records-container">
    <div class="stylecard">
    
    
    <c:forEach var="rec" items="${records}">
    <div class="record-card ${rec.recordType}">
        <div class="record-header">
            <span class="record-date">${rec.date}</span>
            <span class="record-account">${rec.account}</span>
        </div>
        <div class="record-body">
            <div class="record-category">${rec.category}</div>
            <div class="record-description">${rec.description}</div>
        </div>
        <div class="record-amount">
            ${rec.recordType == 'income' ? 'Income' : 'Expense'}: ₹ ${rec.amount}
        </div>
    </div>
</c:forEach>
    
    
    <%-- <c:forEach var="rec" items="${records}">
        <div class="record-card ${rec.amount >= 0 ? 'income' : 'expense'}">
            <div class="record-header">
                <span class="record-date">${rec.date}</span>
                <span class="record-account">${rec.account}</span>
            </div>
            <div class="record-body">
                <div class="record-category">${rec.category}</div>
                <div class="record-description">${rec.description}</div>
            </div>
            <div class="record-amount">
                ${rec.amount >= 0 ? 'Income' : 'Expense'}: ₹ ${rec.amount}
            </div>
        </div>
    </c:forEach> --%>
    </div>
</div>
</div>
</div>


  <script>
    function toggleDropdown() {
      const menu = document.getElementById("dropdownMenu");
      menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }

    // Close dropdown when clicking outside
    window.onclick = function(event) {
      if (!event.target.matches('.avatar-btn') && !event.target.closest('.avatar-container')) {
        document.getElementById("dropdownMenu").style.display = "none";
      }
    }
  </script>

</body>
</html>
