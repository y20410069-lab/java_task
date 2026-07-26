<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // エラーマップや入力値の取得
    java.util.Map<String, java.util.List<String>> errors = 
        (java.util.Map<String, java.util.List<String>>) request.getAttribute("errors");
    String generalError = (String) request.getAttribute("error");
    
    String titleVal = (String) request.getAttribute("title");
    if (titleVal == null) titleVal = "";
    
    String descVal = (String) request.getAttribute("description");
    if (descVal == null) descVal = "";
    
    String statusVal = (String) request.getAttribute("status");
    if (statusVal == null) statusVal = "pending";
    
    String priorityVal = (String) request.getAttribute("priority");
    if (priorityVal == null) priorityVal = "medium";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規タスク登録 - タスク管理システム</title>
<style>
    body {
        font-family: 'Helvetica Neue', Arial, sans-serif;
        background-color: #f4f6f9;
        margin: 0;
        padding: 20px;
        color: #333;
    }
    .container {
        max-width: 600px;
        margin: 40px auto;
        background: #fff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);
    }
    h1 {
        margin-top: 0;
        margin-bottom: 20px;
        color: #495057;
        border-bottom: 2px solid #e9ecef;
        padding-bottom: 10px;
    }
    .form-group {
        margin-bottom: 20px;
    }
    label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
        color: #495057;
    }
    input[type="text"], textarea, select {
        width: 100%;
        padding: 10px;
        border: 1px solid #ced4da;
        border-radius: 4px;
        box-sizing: border-box;
        font-size: 14px;
    }
    textarea {
        height: 100px;
        resize: vertical;
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
    .field-error {
        color: #dc3545;
        font-size: 12px;
        margin-top: 5px;
    }
    .btn-area {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 30px;
    }
    .btn-submit {
        background-color: #007bff;
        color: white;
        border: none;
        padding: 10px 25px;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
    }
    .btn-submit:hover { background-color: #0069d9; }
    .btn-back {
        color: #6c757d;
        text-decoration: none;
        font-size: 14px;
    }
    .btn-back:hover { text-decoration: underline; }
</style>
</head>
<body>

<div class="container">
    <h1>新しいタスクを追加</h1>
    
    <%-- 全体エラーの表示 --%>
    <% if (generalError != null && !generalError.isEmpty()) { %>
        <div class="error-message">
            <%= generalError %>
        </div>
    <% } %>
    
    <form action="<%= request.getContextPath() %>/app/task/new" method="post">
        <div class="form-group">
            <label for="title">タイトル（100文字以内） <span style="color: #dc3545;">*</span></label>
            <input type="text" id="title" name="title" value="<%= titleVal %>" required maxlength="100" placeholder="タスクのタイトルを入力">
            <%-- タイトル専用のエラー表示 --%>
            <% if (errors != null && errors.get("title") != null) { %>
                <div class="field-error">
                    <% for (String err : errors.get("title")) { %>
                        <div><%= err %></div>
                    <% } %>
                </div>
            <% } %>
        </div>
        
        <div class="form-group">
            <label for="description">説明</label>
            <textarea id="description" name="description" placeholder="タスクの詳細説明を入力"><%= descVal %></textarea>
            <%-- 説明専用のエラー表示 --%>
            <% if (errors != null && errors.get("description") != null) { %>
                <div class="field-error">
                    <% for (String err : errors.get("description")) { %>
                        <div><%= err %></div>
                    <% } %>
                </div>
            <% } %>
        </div>
        
        <!-- ValidationUtil (pending|in_progress|completed) に合わせた value 値 -->
        <div class="form-group">
            <label for="status">ステータス</label>
            <select id="status" name="status">
                <option value="pending" <%= ("pending".equals(statusVal) || "未着手".equals(statusVal) || "TODO".equals(statusVal)) ? "selected" : "" %>>未着手</option>
                <option value="in_progress" <%= ("in_progress".equals(statusVal) || "進行中".equals(statusVal) || "DOING".equals(statusVal)) ? "selected" : "" %>>進行中</option>
                <option value="completed" <%= ("completed".equals(statusVal) || "完了".equals(statusVal) || "DONE".equals(statusVal)) ? "selected" : "" %>>完了</option>
            </select>
            <% if (errors != null && errors.get("status") != null) { %>
                <div class="field-error">
                    <% for (String err : errors.get("status")) { %>
                        <div><%= err %></div>
                    <% } %>
                </div>
            <% } %>
        </div>
        
        <!-- ValidationUtil (low|medium|high) に合わせた value 値 -->
        <div class="form-group">
            <label for="priority">優先度</label>
            <select id="priority" name="priority">
                <option value="low" <%= ("low".equals(priorityVal) || "低".equals(priorityVal) || "LOW".equals(priorityVal)) ? "selected" : "" %>>低</option>
                <option value="medium" <%= ("medium".equals(priorityVal) || "中".equals(priorityVal) || "MEDIUM".equals(priorityVal)) ? "selected" : "" %>>中</option>
                <option value="high" <%= ("high".equals(priorityVal) || "高".equals(priorityVal) || "HIGH".equals(priorityVal)) ? "selected" : "" %>>高</option>
            </select>
            <% if (errors != null && errors.get("priority") != null) { %>
                <div class="field-error">
                    <% for (String err : errors.get("priority")) { %>
                        <div><%= err %></div>
                    <% } %>
                </div>
            <% } %>
        </div>
        
        <div class="btn-area">
            <a href="<%= request.getContextPath() %>/app/task/list" class="btn-back">← 一覧に戻る</a>
            <button type="submit" class="btn-submit">タスクを登録する</button>
        </div>
    </form>
</div>

</body>
</html>