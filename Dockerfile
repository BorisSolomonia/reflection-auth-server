FROM maven:latest
WORKDIR /auth-server

COPY . /auth-server
COPY . /src/main/resources/application.yaml
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/auth-server/target/authentication-server-0.0.1-SNAPSHOT.jar"]