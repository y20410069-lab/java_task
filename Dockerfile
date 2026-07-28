# ----------------------------------------------------
# 1. ビルドステージ：MavenでJavaコードをコンパイル
# ----------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# POMファイルとソースコードをコピーしてビルド
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ----------------------------------------------------
# 2. 実行ステージ：Tomcat 11でアプリを起動
# ----------------------------------------------------
FROM tomcat:11.0-jdk21

WORKDIR /usr/local/tomcat

# server.xml 内のシャットダウンポート (port="8005") を無効化
RUN sed -i 's/port="8005"/port="-1"/g' conf/server.xml

# デフォルトアプリを削除
RUN rm -rf webapps/*

# ビルドステージで作成されたクラスファイル・JSP・設定ファイルをROOTへコピー
COPY --from=builder /app/target/classes/ webapps/ROOT/WEB-INF/classes/
COPY src/main/webapp/ webapps/ROOT/

EXPOSE 8080

CMD ["catalina.sh", "run"]