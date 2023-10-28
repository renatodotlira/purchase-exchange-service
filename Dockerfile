FROM openjdk:17-alpine
FROM maven:3.8.5-openjdk-11-slim as builder
COPY ./src src
COPY ./pom.xml /
RUN mvn clean package

FROM openjdk:17-alpine
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "/app.jar"]