FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

ENV QUARKUS_HTTP_HOST=0.0.0.0

COPY pom.xml /app/pom.xml
COPY src /app/src

RUN mvn clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]