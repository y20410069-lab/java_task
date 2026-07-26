package controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Action {
    /**
     * リクエストを処理し、遷移先のパス（またはアクション名）を返します。
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}