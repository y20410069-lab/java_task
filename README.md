# タスク管理システム (Jakarta EE / MVC)

Jakarta EE (Servlet/JSP) と MariaDB を用いて構築した、個人向けタスク管理Webアプリケーションです。
フレームワークに依存せず、Web開発の基本構造（MVCパターン、認証・認可、DAO/Repositoryパターン）を理解・実装することを目的に開発しました。

## 🛠 使用技術 (Tech Stack)
* **Language**: Java 25
* **Server/Framework**: Jakarta EE (Servlet/JSP), Apache Tomcat 11
* **Database**: MariaDB / MySQL (JDBC API)
* **Frontend**: HTML5, CSS3, JSP

## 💡 主な機能
* **ユーザー管理**: ログイン / ログアウト / セッション管理
* **タスクCRUD機能**: 新規作成・一覧表示・詳細・編集・削除
* **認可制御**: ログインユーザーごとに自身のタスクのみアクセス・操作可能
* **検索・ソート・お気に入り**: キーワード検索、作成日時昇順/降順、お気に入りフラグ切り替え

## 🔒 設計上の工夫・こだわり
1. **FrontController によるリクエスト統一管理**
   - リダイレクト処理の標準化とパス補完ロジックを導入し、ルーティング事故を防止。
2. **多層防御による認可（セキュリティ）**
   - Action層・Repository層の双方で `user_id` の検証を行い、URLの直接叩きによる他者データの更新・削除を物理的に遮断。
3. **データマッピング層の設計**
   - 画面側の入力値（日本語表記）とDB側のデータ定義（英字コード値）の乖離をActionクラスで変換・吸収し、UIの使いやすさとDBの整合性を両立。