FROM amazoncorretto:25.0.3
RUN yum install -y shadow-utils
RUN groupadd -r wc_secure_role && useradd -r -g wc_secure_role wc_dev

WORKDIR /app-user-management
RUN chown wc_dev:wc_secure_role /app-user-management
USER wc_dev

ARG JAR_FILE=target/*.jar
COPY --chown=wc_dev:wc_secure_role ${JAR_FILE} appusermanagement-0.0.1-SNAPSHOT.jar
COPY --chown=wc_dev:wc_secure_role ./src/main .

EXPOSE 8080

ENTRYPOINT ["java","-jar", "/app-user-management/appusermanagement-0.0.1-SNAPSHOT.jar"]