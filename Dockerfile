# 1. Apache Tomcat 11 (JDK 21) の公式イメージを使用
FROM tomcat:11.0-jdk21

# 作業ディレクトリをTomcatのルートに設定
WORKDIR /usr/local/tomcat

# デフォルトで入っているサンプルアプリ等を削除
RUN rm -rf webapps/*

# プロジェクト内のWebリソース（HTML/JSP/WEB-INF等）をTomcatのROOTディレクトリへ配置
COPY src/main/webapp/ webapps/ROOT/

# コンテナが使用するポート番号を指定
EXPOSE 8080

# Tomcatを起動
CMD ["catalina.sh", "run"]