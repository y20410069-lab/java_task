# --- STAGE 1: Build ---
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# webapp内の静的リソースおよびWEB-INF構造を準備
COPY src/main/webapp /app

# コンパイル結果の出力先ディレクトリを作成
RUN mkdir -p /app/WEB-INF/classes

# Javaソースコードをコピー
COPY src/main/java /app/src

# TomcatのServlet APIライブラリを取得してjavacでコンパイル
RUN curl -o /tmp/servlet-api.jar https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar && \
    find /app/src -name "*.java" > /tmp/sources.txt && \
    if [ -s /tmp/sources.txt ]; then \
      javac -encoding UTF-8 -cp /tmp/servlet-api.jar -d /app/WEB-INF/classes @/tmp/sources.txt; \
    fi

# --- STAGE 2: Runner ---
FROM tomcat:11.0-jdk21

# デフォルトのROOTアプリケーション等を削除
RUN rm -rf /usr/local/tomcat/webapps/*

# 1. index.jspやWEB-INFなど、webapp配下の全ファイルをwebapps/ROOTへコピー
COPY src/main/webapp/ /usr/local/tomcat/webapps/ROOT/

# 2. ビルドステージでコンパイルしたclassファイルをWEB-INF/classesへ上書き配置
COPY --from=builder /app/WEB-INF/classes /usr/local/tomcat/webapps/ROOT/WEB-INF/classes

# ポート設定
EXPOSE 8080

# Tomcat起動
CMD ["catalina.sh", "run"]