# docker build -t vaomaohao/netty-server:test --build-arg JAR_FILE=server-1.0-SNAPSHOT.jar .
# docker run -p 8464:8464 -e PORT=8464 vaomaohao/netty-server:stable
FROM openjdk:11
MAINTAINER Timofeev Kirill <timofeev.log@narod.ru>
ARG JAR_FILE
ENV PORT 8463
WORKDIR /usr/local/web
COPY ${JAR_FILE} app.jar
CMD ["sh", "-c", "java -Xmx1G -jar -Dport=${PORT} app.jar"]
