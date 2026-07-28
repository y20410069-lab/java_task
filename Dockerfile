# 1. ビルド環境（Mavenを使ってWARファイルを作成）
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. 実行環境（Apache Tomcat 11 上でアプリを動作させる）
FROM tomcat:11.0-jdk21
WORKDIR /usr/local/tomcat

# 不要なデフォルトアプリを削除
RUN rm -rf webapps/*

# ビルドしたWARファイルをTomcatのROOT（ルート）として配置
COPY --from=builder /app/target/*.war webapps/ROOT.war

# ポート番号の設定
EXPOSE 8080

CMD ["catalina.sh", "run"]