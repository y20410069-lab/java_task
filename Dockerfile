# Apache Tomcat 11 の公式イメージを使用
FROM tomcat:11.0-jdk21

WORKDIR /usr/local/tomcat

# 不要なデフォルトアプリを削除
RUN rm -rf webapps/*

# src/main/webapp 配下のWebリソースをTomcatのROOTアプリへコピー
COPY src/main/webapp/ webapps/ROOT/

EXPOSE 8080

CMD ["catalina.sh", "run"]