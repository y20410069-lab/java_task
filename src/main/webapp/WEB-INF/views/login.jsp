<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン - タスク管理システム</title>
<style>
    body {
        font-family: 'Helvetica Neue', Arial, sans-serif;
        background-color: #f4f6f9;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .login-container {
        background-color: #ffffff;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        width: 360px;
    }
    h2 {
        text-align: center;
        margin-bottom: 24px;
        color: #333333;
    }
    .form-group {
        margin-bottom: 20px;
    }
    label {
        display: block;
        margin-bottom: 8px;
        color: #666666;
        font-size: 14px;
    }
    input[type="text"], input[type="password"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #cccccc;
        border-radius: 4px;
        box-sizing: border-box;
        font-size: 16px;
    }
    input[type="text"]:focus, input[type="password"]:focus {
        border-color: #007bff;
        outline: none;
    }
    .error-message {
        color: #dc3545;
        background-color: #f8d7da;
        border: 1px solid #f5c6cb;
        padding: 10px;
        border-radius: 4px;
        margin-bottom: 20px;
        font-size: 14px;
    }
    button {
        width: 100%;
        padding: 12px;
        background-color: #007bff;
        border: none;
        color: white;
        font-size: 16px;
        font-weight: bold;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.2s;
    }
    button:hover {
        background-color: #0056b3;
    }
</style>
</head>
<body>

<div class="login-container">
    <h2>タスク管理システム</h2>
    
    <%-- LoginActionから送られてきたエラーメッセージがあれば赤枠で表示 --%>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <%-- ログインフォーム（POSTメソッドで自分自身のアクションへ送信） --%>
    <form action="<%= request.getContextPath() %>/app/login" method="POST">
        <div class="form-group">
            <label for="username">ユーザー名</label>
            <%-- UX配慮：以前入力されたユーザー名があればvalueに自動セット --%>
            <input type="text" id="username" name="username" 
                   value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" required>
        </div>
        
        <div class="form-group">
            <label for="password">パスワード</label>
            <input type="password" id="password" name="password" required>
        </div>
        
        <button type="submit">ログイン</button>
    </form>
</div>

</body>
</html>