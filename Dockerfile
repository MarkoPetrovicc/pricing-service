FROM openjdk:11
EXPOSE 8080
COPY target/message-server.jar message-server.jar
ENTRYPOINT ["java","-jar","/message-server.jar"]