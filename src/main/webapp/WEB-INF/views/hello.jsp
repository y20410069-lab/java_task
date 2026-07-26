<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>動作確認画面</title>
<style>
    body { font-family: sans-serif; background-color: #f5f5f5; padding: 20px; }
    .container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
    .info-box { background: #eef2f3; padding: 15px; border-radius: 5px; margin: 10px 0; }
    .btn { display: inline-block; background: #007bff; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; margin: 5px 2px; }
</style>
</head>
<body>
<div class="container">
    <h1>FrontController 動作確認</h1>
    
    <div class="info-box">
        <h3>メッセージ表示</h3>
        <p>${message}</p>
    </div>
    
    <div class="info-box">
        <h3>詳細情報</h3>
        <p><strong>現在時刻:</strong> ${currentTime}</p>
        <p><strong>パス情報:</strong> ${pathInfo}</p>
        <p><strong>リモートアドレス:</strong> ${remoteAddr}</p>
        <p><strong>ブラウザ情報 (User-Agent):</strong> ${userAgent}</p>
    </div>

    <div class="info-box">
        <h3>動作確認リンク</h3>
        <a class="btn" href="${pageContext.request.contextPath}/app/hello">再表示</a>
        <a class="btn" href="${pageContext.request.contextPath}/app/">デフォルトアクション</a>
        <a class="btn" href="${pageContext.request.contextPath}/app/hello?name=田中">パラメータ付き</a>
        <a class="btn" href="${pageContext.request.contextPath}/app/unknown">404テスト</a>
    </div>
</div>
</body>
</html>