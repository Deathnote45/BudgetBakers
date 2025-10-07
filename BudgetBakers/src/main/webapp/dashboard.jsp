<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        body { font-family: Arial; background-color: #f4f6f8; margin: 0; padding: 0; }

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

        /* Dashboard Styles */
        .container { width: 90%; margin: 2rem auto; }
        .cards { display: flex; gap: 1rem; flex-wrap: wrap; margin-bottom: 2rem; }
        .card { flex: 1 1 30%; background: white; padding: 1rem; border-radius: 10px; box-shadow: 0 3px 6px rgba(0,0,0,0.1); text-align: center; }
        table { width: 100%; border-collapse: collapse; background: white; margin-bottom: 2rem; border-radius: 10px; overflow: hidden; }
        th, td { padding: 12px 15px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        h3 { margin-top: 1rem; }
        .chart-container { background: white; padding: 1rem; border-radius: 10px; box-shadow: 0 3px 6px rgba(0,0,0,0.1); margin-bottom: 2rem; }
    </style>
</head>
<body>

<!-- Navbar -->
<div class="navbar">
    <div class="navbar-left">
        <!-- <img src="logo.svg" alt="Logo"> -->
         <img src="${pageContext.request.contextPath}/images/logo.svg" alt="BudgetBakers">
        <div class="nav-links">
            <a href="DashboardServlet">Dashboard</a>
           <!--  <a href="AccountsServlet">Accounts</a> -->
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

<div class="container">

    <!-- Summary Cards -->
    <div class="cards">
        <div class="card">
            <h3>Total Income</h3>
            <p>$<c:out value="${totalIncome}" /></p>
        </div>
        <div class="card">
            <h3>Total Expense</h3>
            <p>$<c:out value="${totalExpense}" /></p>
        </div>
        <div class="card">
            <h3>Net Amount</h3>
            <p>$<c:out value="${netAmount}" /></p>
        </div>
    </div>

    <!-- Category-wise Tables -->
    <h3>Category-wise Income</h3>
    <table>
        <thead>
            <tr>
                <th>Category</th>
                <th>Total Income</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${categoryIncome}">
                <tr>
                    <td><c:out value="${entry.key}" /></td>
                    <td>$<c:out value="${entry.value}" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h3>Category-wise Expense</h3>
    <table>
        <thead>
            <tr>
                <th>Category</th>
                <th>Total Expense</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${categoryExpense}">
                <tr>
                    <td><c:out value="${entry.key}" /></td>
                    <td>$<c:out value="${entry.value}" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Chart -->
    <div class="chart-container">
        <canvas id="categoryChart" width="800" height="400"></canvas>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Prepare data from JSP
    const categories = [
        <c:forEach var="entry" items="${categoryIncome}">"${entry.key}",</c:forEach>
    ];

    const incomeData = [
        <c:forEach var="entry" items="${categoryIncome}">${entry.value},</c:forEach>
    ];

    const expenseData = [
        <c:forEach var="entry" items="${categoryExpense}">
            ${entry.value != null ? entry.value : 0},
        </c:forEach>
    ];

    const ctx = document.getElementById('categoryChart').getContext('2d');
    const categoryChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: categories,
            datasets: [
                {
                    label: 'Income',
                    data: incomeData,
                    backgroundColor: 'rgba(75, 192, 192, 0.7)'
                },
                {
                    label: 'Expense',
                    data: expenseData,
                    backgroundColor: 'rgba(255, 99, 132, 0.7)'
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'top' },
                title: {
                    display: true,
                    text: 'Category-wise Income vs Expense'
                }
            },
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
</script>

</body>
</html>
