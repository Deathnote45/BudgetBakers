<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.budgetbakers.entity.Accounts" %>
<%@ page session="true" %>
<%
    List<Accounts> accounts = (List<Accounts>) request.getAttribute("accounts");
    String message = (String) session.getAttribute("message");
    if (message != null) {
        session.removeAttribute("message");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Accounts</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background: #f8f9fa; }

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

        /* Page Content */
        .content { padding: 20px; }

        table { width: 100%; border-collapse: collapse; margin-top: 20px; background: #fff; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
        th { background: #f4f4f4; }
        .message { color: green; font-weight: bold; }
        .btn { padding: 5px 10px; margin: 2px; text-decoration: none; border: 1px solid #333; border-radius: 4px; }
        .btn-add { background: #4CAF50; color: white; }
        .btn-edit { background: #2196F3; color: white; }
        .btn-delete { background: #f44336; color: white; }
        form { margin-top: 20px; }
        input, select { padding: 5px; margin: 5px; }
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
            <!-- <a href="AccountsServlet">Accounts</a> -->
            <a href="${pageContext.request.contextPath}/accounts">Accounts</a>
            
            <a href="ViewRecordsServlet">Records</a>
            
        </div>
    </div>

    <div class="avatar-container">
        <button class="avatar-btn" onclick="toggleDropdown()">
           <!--  <img src="avatar.png" alt="Shuvangi"> -->
            <img src="${pageContext.request.contextPath}/images/avatar.png" alt="Shuvangi">
        </button>
        <div class="dropdown" id="dropdownMenu">
            <p><strong>${sessionScope.email}</strong></p>
            <a href="#">Settings</a>
            <a href="logout.jsp">Logout</a>
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

<div class="content">

    <h2>My Accounts</h2>

    <% if (message != null) { %>
        <p class="message"><%= message %></p>
    <% } %>

    <!-- Add Account Form -->
    <h3>Add New Account</h3>
    <form action="accounts" method="post">
        <input type="hidden" name="action" value="add">
        <input type="text" name="name" placeholder="Account Name" required>
        <select name="type" required>
            <option value="">Select Type</option>
            <option value="Savings">Savings</option>
            <option value="Checking">Checking</option>
            <option value="Credit">Credit</option>
        </select>
        <input type="number" step="0.01" name="balance" placeholder="Balance" required>
        <button type="submit" class="btn btn-add">Add Account</button>
    </form>

    <!-- Accounts Table -->
    <h3>Existing Accounts</h3>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Type</th>
                <th>Balance</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% if (accounts != null) {
                for (Accounts acc : accounts) { %>
                    <tr>
                        <td><%= acc.getId() %></td>
                        <td><%= acc.getName() %></td>
                        <td><%= acc.getType() %></td>
                        <td><%= acc.getBalance() %></td>
                        <td>
                            <!-- Edit Form -->
                            <form action="accounts" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="id" value="<%= acc.getId() %>">
                                <input type="text" name="name" value="<%= acc.getName() %>" required>
                                <select name="type" required>
                                    <option value="Savings" <%= "Savings".equals(acc.getType()) ? "selected" : "" %>>Savings</option>
                                    <option value="Checking" <%= "Checking".equals(acc.getType()) ? "selected" : "" %>>Checking</option>
                                    <option value="Credit" <%= "Credit".equals(acc.getType()) ? "selected" : "" %>>Credit</option>
                                </select>
                                <input type="number" step="0.01" name="balance" value="<%= acc.getBalance() %>" required>
                                <button type="submit" class="btn btn-edit">Update</button>
                            </form>

                            <!-- Delete Form -->
                            <form action="accounts" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= acc.getId() %>">
                                <button type="submit" class="btn btn-delete" onclick="return confirm('Are you sure?')">Delete</button>
                            </form>
                        </td>
                    </tr>
            <%  }
            } else { %>
                <tr><td colspan="5">No accounts found.</td></tr>
            <% } %>
        </tbody>
    </table>

</div>

</body>
</html>
