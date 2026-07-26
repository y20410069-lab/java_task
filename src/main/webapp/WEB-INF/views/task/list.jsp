<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String username = "ゲスト";
    Object loginUserObj = request.getAttribute("loginUser");
    if (loginUserObj != null) {
        username = ((model.User)loginUserObj).getUsername();
    }
    if (session.getAttribute("loginUser") != null) {
        username = ((model.User)session.getAttribute("loginUser")).getUsername();
    }
    
    String errorMsg = (String) request.getAttribute("error");
    if (errorMsg == null) {
        errorMsg = (String) session.getAttribute("error");
        session.removeAttribute("error");
    }
    String keywordVal = (String) request.getAttribute("keyword");
    if (keywordVal == null) keywordVal = "";
    String sortVal = (String) request.getAttribute("sort");
    if (sortVal == null) sortVal = "DESC";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タスク一覧 - タスク管理システム</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<style>
    body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 20px; color: #333; }
    .container { max-width: 1000px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
    .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #e9ecef; padding-bottom: 20px; margin-bottom: 20px; }
    h1 { margin: 0; color: #495057; }
    .user-info { font-size: 14px; color: #6c757d; }
    .btn-create { display: inline-block; background-color: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }
    .btn-logout { display: inline-block; background-color: #6c757d; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; font-size: 14px; font-weight: bold; }
    .btn-logout:hover { background-color: #5a6268; }
    .search-box { margin-bottom: 15px; padding: 15px; background: #f8f9fa; border-radius: 5px; }
    .filter-section { display: flex; gap: 10px; margin-bottom: 20px; }
    .filter-btn { padding: 6px 14px; font-size: 14px; border: 1px solid #ced4da; background-color: #e9ecef; color: #495057; border-radius: 4px; cursor: pointer; text-decoration: none; }
    .filter-btn.active { background-color: #007bff; color: white; border-color: #007bff; }
    .task-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
    .task-table th, .task-table td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #dee2e6; }
    .task-table th { background-color: #f8f9fa; color: #495057; }
    .status-badge { display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
    .status-todo { background-color: #e9ecef; color: #495057; }
    .status-doing { background-color: #cce5ff; color: #004085; }
    .status-done { background-color: #d4edda; color: #155724; }
    .priority-high { color: #dc3545; font-weight: bold; }
    .priority-medium { color: #ffc107; font-weight: bold; }
    .priority-low { color: #6c757d; }
    .empty-message { text-align: center; padding: 40px; color: #6c757d; font-size: 16px; }
    .btn { padding: 5px 10px; margin: 0 2px; text-decoration: none; border-radius: 3px; font-size: 12px; display: inline-block; color: white; }
    .btn-edit { background-color: #007bff; }
    .btn-edit:hover { background-color: #0069d9; }
    .btn-delete { background-color: #dc3545; }
    .btn-delete:hover { background-color: #c82333; }
    .paging-info { margin: 15px 0 5px 0; font-size: 14px; color: #6c757d; }
    .pagination { display: flex; gap: 5px; align-items: center; margin-top: 15px; }
    .pagination a, .pagination span { padding: 6px 12px; font-size: 14px; text-decoration: none; border: 1px solid #dee2e6; border-radius: 3px; }
    .pagination a { color: #007bff; background-color: #fff; }
    .pagination a:hover { background-color: #e9ecef; }
    .pagination span.current { color: #fff; background-color: #007bff; border-color: #007bff; }
    .pagination span.disabled { color: #6c757d; background-color: #f8f9fa; border-color: #dee2e6; }
    
    .favorite-btn { text-decoration: none; font-size: 18px; transition: transform 0.2s ease; display: inline-block; }
    .favorite-btn:hover { transform: scale(1.2); }
    .favorite-active { color: #ffc107; }
    .favorite-inactive { color: #6c757d; }
    .favorite-task { background-color: #fffdf5; border-left: 4px solid #ffc107; }
</style>
</head>
<body>

<div class="container">
    <div class="header">
        <div>
            <h1>タスク一覧</h1>
            <div class="user-info">ログインユーザー: <strong><%= username %></strong> さん</div>
        </div>
        <div style="display: flex; gap: 10px; align-items: center;">
            <a href="<%= request.getContextPath() %>/app/task/new" class="btn-create">＋ 新しいタスクを追加</a>
            <a href="<%= request.getContextPath() %>/app/logout" class="btn-logout">ログアウト</a>
        </div>
    </div>

    <div class="search-box">
        <form action="<%= request.getContextPath() %>/app/task/list" method="get" style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
            <div>
                <label for="keyword" style="font-size: 14px; margin-right: 5px;">キーワード:</label>
                <input type="text" id="keyword" name="keyword" value="${keyword}" placeholder="タスク名で検索" style="padding: 5px; font-size: 14px; border: 1px solid #ccc; border-radius: 3px;">
            </div>

            <div>
                <label for="sort" style="font-size: 14px; margin-right: 5px;">並び替え:</label>
                <select id="sort" name="sort" style="padding: 5px; font-size: 14px; border: 1px solid #ccc; border-radius: 3px;">
                    <option value="DESC" ${sort == 'DESC' ? 'selected' : ''}>作成日：新しい順</option>
                    <option value="ASC" ${sort == 'ASC' ? 'selected' : ''}>作成日：古い順</option>
                </select>
            </div>

            <input type="hidden" name="page" value="1">

            <button type="submit" style="padding: 6px 12px; background-color: #28a745; color: white; border: none; border-radius: 3px; font-size: 14px; cursor: pointer;">検索・反映</button>
            <a href="<%= request.getContextPath() %>/app/task/list" style="font-size: 14px; color: #007bff; text-decoration: none; margin-left: 10px;">リセット</a>
        </form>
    </div>

    <div class="filter-section">
        <button type="button" id="filter-all" class="filter-btn active" onclick="showAllTasks()">すべて表示</button>
        <button type="button" id="filter-favorite" class="filter-btn" onclick="showFavoritesOnly()">お気に入りのみ</button>
    </div>

    <% if (errorMsg != null && !errorMsg.isEmpty()) { %>
        <div style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 20px; font-size: 14px;">
            <%= errorMsg %>
        </div>
    <% } %>

    <c:if test="${totalRecords > 0}">
        <div class="paging-info">
            <p>全 ${totalRecords} 件中 ${startRecord}-${endRecord} 件目を表示 (${currentPage}/${totalPages} ページ)</p>
        </div>
    </c:if>

    <c:if test="${not empty tasks}">
        <table class="task-table">
            <thead>
                <tr>
                    <th style="width: 80px; text-align: center;">お気に入り</th>
                    <th>タイトル</th>
                    <th>説明</th>
                    <th>ステータス</th>
                    <th>優先度</th>
                    <th style="width: 110px;">作成日</th>
                    <th style="width: 140px;">操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="currentTask" items="${tasks}">
                    <tr class="task-row ${currentTask.favorite ? 'favorite-task' : ''}" data-favorite="${currentTask.favorite}">
                        <td style="text-align: center;">
                            <a href="<%= request.getContextPath() %>/app/favorite/toggle?taskId=${currentTask.id}" class="favorite-btn">
                                <c:choose>
                                    <c:when test="${currentTask.favorite}">
                                        <i class="fa-solid fa-star favorite-active" title="お気に入り解除"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa-regular fa-star favorite-inactive" title="お気に入り登録"></i>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                        <td><strong><c:out value="${currentTask.title}"/></strong></td>
                        <td><c:out value="${currentTask.description}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${currentTask.status == 'pending' || currentTask.status == '未着手'}">
                                    <span class="status-badge status-todo">未着手</span>
                                </c:when>
                                <c:when test="${currentTask.status == 'in_progress' || currentTask.status == '進行中'}">
                                    <span class="status-badge status-doing">進行中</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-done">完了</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${currentTask.priority == 'high' || currentTask.priority == '高'}">
                                    <span class="priority-high">高</span>
                                </c:when>
                                <c:when test="${currentTask.priority == 'medium' || currentTask.priority == '中'}">
                                    <span class="priority-medium">中</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="priority-low">低</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${currentTask.createdAt}"/></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/app/task/edit?id=${currentTask.id}" class="btn btn-edit">編集</a>
                            <a href="<%= request.getContextPath() %>/app/task/delete?id=${currentTask.id}" class="btn btn-delete" onclick="return confirmDelete('${currentTask.title}');">削除</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="?keyword=${keyword}&sort=${sort}&page=${currentPage - 1}">« 前へ</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">« 前へ</span>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?keyword=${keyword}&sort=${sort}&page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="?keyword=${keyword}&sort=${sort}&page=${currentPage + 1}">次へ »</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">次へ »</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </c:if>

    <c:if test="${empty tasks}">
        <div class="empty-message">
            登録されているタスクはありません。上のボタンから新しいタスクを追加しましょう！
        </div>
    </c:if>
</div>

<script>
function confirmDelete(taskTitle) {
    return confirm('タスク 「' + taskTitle + '」を本当に削除しますか？\n\n削除したデータは復元できません。');
}

function showAllTasks() {
    const rows = document.querySelectorAll('.task-row');
    rows.forEach(row => row.style.display = '');
    setActiveFilter(document.getElementById('filter-all'));
}

function showFavoritesOnly() {
    const rows = document.querySelectorAll('.task-row');
    rows.forEach(row => {
        const isFav = row.getAttribute('data-favorite') === 'true';
        row.style.display = isFav ? '' : 'none';
    });
    setActiveFilter(document.getElementById('filter-favorite'));
}

function setActiveFilter(activeButton) {
    document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
    activeButton.classList.add('active');
}
</script>

</body>
</html>