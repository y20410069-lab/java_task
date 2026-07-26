package database;

import java.util.List;

import model.Task;  // ★新しく作った正しいTaskDTOを読み込む
import model.User;  // ★新しく作った正しいUserDTOを読み込む
import repository.TaskRepository;
import repository.UserRepository;

public class ConnectionTest {
    public static void main(String[] args) {
        System.out.println("====== 新リポジトリ層・総合動作確認テスト ======");

        // リポジトリのインスタンス化
        UserRepository userRepo = new UserRepository();
        TaskRepository taskRepo = new TaskRepository();

        // --------------------------------------------------------
        // テスト1: ユーザー全員 of 取得 (UserRepository.findAll)
        // --------------------------------------------------------
        System.out.println("\n--- [テスト1] ユーザー一覧取得 ---");
        List<User> userList = userRepo.findAll();
        System.out.println("取得件数: " + userList.size() + " 件");
        for (User u : userList) {
            // DTOで定義した toString() メソッドが自動的に呼ばれます
            System.out.println(u);
        }

        // --------------------------------------------------------
        // テスト2: 特定ユーザーのピンポイント検索 (UserRepository.findById)
        // --------------------------------------------------------
        System.out.println("\n--- [テスト2] ユーザー単件検索 (ID: 1) ---");
        User targetUser = userRepo.findById(1);
        if (targetUser != null) {
            System.out.println("発見: " + targetUser.getUsername() + " (" + targetUser.getEmail() + ")");
        } else {
            System.out.println("指定されたユーザーが見つかりません。");
        }

        // --------------------------------------------------------
        // テスト3: タスク全員の取得 (TaskRepository.findAll)
        // --------------------------------------------------------
        System.out.println("\n--- [テスト3] タスク一覧取得 ---");
        List<Task> taskList = taskRepo.findAll();
        System.out.println("取得件数: " + taskList.size() + " 件");
        for (Task t : taskList) {
            System.out.println(t + " [優先度: " + t.getPriority() + "]");
        }

        // --------------------------------------------------------
        // テスト4: 特定ユーザーに紐づくタスクの抽出 (TaskRepository.findByUserId)
        // --------------------------------------------------------
        System.out.println("\n--- [テスト4] ユーザーID: 1 に紐づくタスク抽出 ---");
        List<Task> userTasks = taskRepo.findByUserId(1);
        System.out.println("ユーザーID:1 のタスク件数: " + userTasks.size() + " 件");
        for (Task t : userTasks) {
            System.out.println(" ・ " + t.getTitle() + " (ステータス: " + t.getStatus() + ")");
        }

        System.out.println("\n================================================");
    }
}