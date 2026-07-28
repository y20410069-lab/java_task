# Apache Tomcat 11 (JDK 21) の公式イメージを使用
FROM tomcat:11.0-jdk21

WORKDIR /usr/local/tomcat

# server.xml 内のシャットダウンポート (port="8005") を port="-1" に置き換えて無効化
RUN sed -i 's/port="8005"/port="-1"/g' conf/server.xml

# デフォルトアプリを削除
RUN rm -rf webapps/*

# 1. HTML/JSP/WEB-INF(web.xml) などをROOTへコピー
COPY src/main/webapp/ webapps/ROOT/

# 2. Mavenのビルド済みクラスファイル(.class)を WEB-INF/classes へコピー
COPY target/classes/ webapps/ROOT/WEB-INF/classes/

EXPOSE 8080

CMD ["catalina.sh", "run"]