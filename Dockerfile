FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /app

COPY . /app

RUN mvn clean package

FROM openjdk:18-slim

WORKDIR /app

COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar /app/backend-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/backend-0.0.1-SNAPSHOT.jar"]











