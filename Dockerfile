# Apache Tomcat 11 の公式イメージを使用
FROM tomcat:11.0-jdk21

WORKDIR /usr/local/tomcat

# 不要なデフォルトアプリを削除
RUN rm -rf webapps/*

# EclipseのWebコンテンツディレクトリ（WebContentまたはsrc/main/webapp）をROOTにコピー
# WebContent の場合は下の行をそのまま使用します
COPY WebContent/ webapps/ROOT/

# ※もしプロジェクトのフォルダ名が WebContent ではなく src/main/webapp の場合は
# 上の行をコメントアウトし、下の行の # を消してください
# COPY src/main/webapp/ webapps/ROOT/

EXPOSE 8080

CMD ["catalina.sh", "run"]