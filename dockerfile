FROM maven:3.9-amazoncorretto-25-alpine AS builder

WORKDIR /app

COPY pom.xml .

COPY src/main ./src/main

RUN ./mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:25-alpine AS runner

WORKDIR /app

COPY --from=builder /app/target/app-user-management-0.0.1-SNAPSHOT.jar app-user-management-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar", "/app/app-user-management-0.0.1-SNAPSHOT.jar"]