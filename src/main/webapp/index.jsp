<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // ルート（/）にアクセスがあったら /app/login へリダイレクト
    response.sendRedirect(request.getContextPath() + "/app/login");
%>