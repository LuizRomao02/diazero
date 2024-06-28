FROM openjdk:17-jdk-alpine

ADD target/diazero-00.00.01.war app.war

ENTRYPOINT ["java", "-jar", "/app.war"]

EXPOSE 8080