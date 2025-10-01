# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY common-libs/pom.xml common-libs/
COPY auth-service/pom.xml auth-service/
RUN mvn -B dependency:go-offline
COPY . .
RUN mvn -pl auth-service -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/auth-service/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
