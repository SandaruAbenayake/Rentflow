FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml pom.xml
COPY backend backend
ARG SERVICE_DIR
RUN mvn -f "${SERVICE_DIR}/pom.xml" package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
ARG SERVICE_DIR
COPY --from=build /workspace/${SERVICE_DIR}/target/*-exec.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
