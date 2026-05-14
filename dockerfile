FROM amazoncorretto:25.0.3
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} appusermanagement-0.0.1-SNAPSHOT.jar
COPY ./src .
ENTRYPOINT ["java","-jar", "/appusermanagement-0.0.1-SNAPSHOT.jar"]