<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%
    // Invalidate the current session
    session.invalidate();

    // Redirect the user back to login page
    response.sendRedirect("login.jsp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Logging Out...</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 100px;
            background-color: #f5f5f5;
        }
        h1 {
            color: #3aafa9;
        }
        p {
            color: #555;
        }
    </style>
</head>
<body>
    <h1>Logging out...</h1>
    <p>If you are not redirected automatically, <a href="login.jsp">click here</a>.</p>
</body>
</html>
