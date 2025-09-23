<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - BudgetBakers</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 60%;
            margin: 50px auto;
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        h1 {
            color: #3aafa9;
        }
        p {
            font-size: 1.1em;
            color: #333;
        }
        .logout-btn {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #2b7a78;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
         <h1>Welcome, <%= session.getAttribute("email") %>!</h1> 
        <p>Your email: <%= session.getAttribute("email") %></p>
        <%-- <p>Your User ID: <%= session.getAttribute("userId") %></p> --%>
        <a href="logout.jsp" class="logout-btn">Logout</a>
    </div>
</body>
</html>
