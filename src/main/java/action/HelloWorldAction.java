package action;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloWorldAction implements controller.Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // 1. パラメータ処理 (nameパラメータがある場合の挨拶メッセージ生成)
        String name = request.getParameter("name");
        String message = "Hello World! FrontController基盤の構築に成功しました。";
        if (name != null && !name.isEmpty()) {
            message = name + "さん、こんにちは！FrontControllerから挨拶を返しています。";
        }
        
        // 2. 表示用データ準備 & リクエストスコープ設定
        request.setAttribute("message", message);
        request.setAttribute("currentTime", new Date().toString());
        request.setAttribute("pathInfo", request.getPathInfo());
        request.setAttribute("userAgent", request.getHeader("User-Agent"));
        request.setAttribute("remoteAddr", request.getRemoteAddr());
        
        // 3. JSPパス返却 (FrontController側で /WEB-INF/views/ と .jsp が補完されるため "hello" を返す)
        return "hello";
    }
}