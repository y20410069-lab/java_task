# Apache Tomcat 11 (JDK 21) の公式イメージを使用
FROM tomcat:11.0-jdk21

WORKDIR /usr/local/tomcat

# デフォルトアプリを削除
RUN rm -rf webapps/*

# 1. HTML/JSP/WEB-INF(web.xml) などをROOTへコピー
COPY src/main/webapp/ webapps/ROOT/

# 2. ビルド済みクラスファイル(.class)を WEB-INF/classes へコピー
COPY build/classes/ webapps/ROOT/WEB-INF/classes/

EXPOSE 8080

CMD ["catalina.sh", "run"]