<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${errorTitle} - タスク管理システム</title>
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
    .error-container {
        background-color: #ffffff;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        width: 480px;
        text-align: center;
    }
    h1 {
        color: #dc3545;
        font-size: 28px;
        margin-bottom: 16px;
    }
    .error-message {
        color: #333333;
        font-size: 16px;
        margin-bottom: 24px;
        line-height: 1.6;
    }
    .suggested-action {
        background-color: #e9ecef;
        padding: 15px;
        border-radius: 4px;
        color: #495057;
        font-size: 14px;
        margin-bottom: 30px;
        text-align: left;
        border-left: 4px solid #6c757d;
    }
    .btn-home {
        display: inline-block;
        padding: 12px 24px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        font-weight: bold;
        border-radius: 4px;
        transition: background-color 0.2s;
    }
    .btn-home:hover {
        background-color: #0056b3;
    }
</style>
</head>
<body>

<div class="error-container">
    <h1>${errorTitle}</h1>
    
    <div class="error-message">
        ${errorMessage}
    </div>
    
    <div class="suggested-action">
        <strong>次をお試しください：</strong><br>
        ${suggestedAction}
    </div>
    
    <a href="<%= request.getContextPath() %>/app/login" class="btn-home">ログイン画面へ戻る</a>
</div>

</body>
</html>