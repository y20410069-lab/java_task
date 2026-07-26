package action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Action;
import model.User;
import repository.UserRepository;
import util.SessionUtil;

/**
 * ログイン画面の表示および認証処理を行うアクションクラスです。
 */
public class LoginAction implements Action {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();

        if ("POST".equals(method)) {
            return processLogin(request);
        }
        
        return showLoginForm(request);
    }

    private String showLoginForm(HttpServletRequest request) {
        if (SessionUtil.isLoggedIn(request)) {
            // 修正: /java_task を削って /app/task/list にする
            return "redirect:/app/task/list";
        }
        return "login";
    }

    private String processLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "ユーザー名とパスワードを入力してください。");
            request.setAttribute("username", username);
            return "login";
        }

        User user = null;
        if ("admin".equals(username)) {
            user = userRepository.findById(1);
        }

        if (user == null || !username.equals(user.getUsername())) {
            request.setAttribute("error", "ユーザー名またはパスワードが正しくありません。");
            request.setAttribute("username", username);
            return "login";
        }

        SessionUtil.login(request, user);
        // 修正: /java_task を削って /app/task/list にする
        return "redirect:/app/task/list";
    }
}