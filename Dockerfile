# ----------------------------------------------------
# 1. ビルドステージ：JDK21でJavaソースコードをコンパイル
# ----------------------------------------------------
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# ソースコードとWebリソースをコピー
COPY src ./src

# TomcatのServlet API JARを取得してコンパイル（lib配下のJARも含める）
RUN mkdir -p build/classes && \
    curl -o servlet-api.jar https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar && \
    find src/main/java -name "*.java" > sources.txt && \
    javac -encoding UTF-8 -d build/classes -classpath "servlet-api.jar:src/main/webapp/WEB-INF/lib/*" @sources.txt

# ----------------------------------------------------
# 2. 実行ステージ：Tomcat 11でアプリを起動
# ----------------------------------------------------
FROM tomcat:11.0-jdk21

WORKDIR /usr/local/tomcat

# server.xml 内のシャットダウンポート (port="8005") を無効化
RUN sed -i 's/port="8005"/port="-1"/g' conf/server.xml

# デフォルトアプリを削除
RUN rm -rf webapps/*

# HTML/JSP/WEB-INF などのWebリソースをROOTへコピー
COPY src/main/webapp/ webapps/ROOT/

# コンパイル済みの .class ファイルを WEB-INF/classes へコピー
COPY --from=builder /app/build/classes/ webapps/ROOT/WEB-INF/classes/

EXPOSE 8080

CMD ["catalina.sh", "run"]