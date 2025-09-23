<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
    body, html {
        height: 100%;
        margin: 0;
        font-family: Arial, sans-serif;
    }
    .container {
        display: flex;
        height: 100%;
    }
    .left-panel {
        background: linear-gradient(to bottom, #2b7a78, #3aafa9);
        color: white;
        width: 40%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        text-align: center;
        padding: 20px;
    }
    .right-panel {
        width: 60%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        padding: 20px;
    }
    .logo {
        position: absolute;
        top: 20px;
        left: 20px;
        color: white;
        font-size: 24px;
        font-weight: bold;
    }
    .left-panel h1 {
        font-size: 2.5em;
        margin-bottom: 20px;
    }
    .left-panel p {
        font-size: 1.2em;
        line-height: 1.5;
        max-width: 80%;
    }
    .form-box {
        width: 80%;
        max-width: 400px;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        background: white;
    }
    h2 {
        font-size: 2em;
        margin-bottom: 20px;
        text-align: center;
    }
    .divider {
        text-align: center;
        margin: 20px 0;
        color: #888;
    }
    .divider::before, .divider::after {
        content: '';
        display: inline-block;
        width: 40%;
        height: 1px;
        background: #ccc;
        vertical-align: middle;
        margin: 0 5px;
    }
    .form-group {
        margin-bottom: 20px;
    }
    .form-group input {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }
    .form-group button {
        width: 100%;
        padding: 12px;
        background-color: #3aafa9;
        color: white;
        border: none;
        border-radius: 5px;
        font-size: 1.1em;
        cursor: pointer;
    }
    .error-message {
        color: red;
        font-size: 0.9em;
        margin-bottom: 10px;
    }
</style>
</head>
<body>
<div class="container">
    <div class="left-panel">
        <div class="logo">budgetbakers</div>
        <h1>Your Finances in One Place</h1>
        <p>Dive into reports, build budgets, sync with your banks and enjoy automatic categorization. Learn more about how Wallet works.</p>
        <img src="https://placehold.co/400x200/cccccc/333333?text=Device+Mockup" alt="Device Mockup" style="max-width: 90%; margin-top: 30px;">
    </div>
    <div class="right-panel">
        <div class="form-box">
            <h2>Log In</h2>

            <!-- ✅ Google Sign-In -->
            <div id="g_id_onload"
                 data-client_id="264389169162-dgq1nvs9lofsg6tlekdvotvtkcctgl3q.apps.googleusercontent.com"
                 data-context="signin"
                 data-ux_mode="redirect"
                 data-login_uri="http://localhost:8080/BudgetBakers/GoogleSignInServlet"
                 data-auto_prompt="false">
            </div>
            <div class="g_id_signin"
                 data-type="standard"
                 data-shape="rectangular"
                 data-theme="outline"
                 data-text="signin_with"
                 data-size="large"
                 data-logo_alignment="left">
            </div>

            <div class="divider">or</div>

            <!-- Normal Email Login -->
            <form action="LoginServlet" method="post">
                <div class="form-group">
                    <label for="email">E-mail</label>
                    <input type="email" id="email" name="email" placeholder="john.doe@budgetbakers.com" required>
                </div>
                <div class="form-group">
                    <button type="submit">Log In</button>
                </div>
                <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                <% if (errorMessage != null) { %>
                    <div class="error-message"><%= errorMessage %></div>
                <% } %>
            </form>
        </div>
    </div>
</div>

<!-- ✅ Google Sign-In JS -->
<script src="https://accounts.google.com/gsi/client" async defer></script>

</body>
</html>
