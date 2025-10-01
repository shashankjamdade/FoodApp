# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only what’s needed
COPY pom.xml .
COPY common-libs/pom.xml common-libs/
COPY auth-service/pom.xml auth-service/

# Download dependencies only for auth-service (and deps)
RUN mvn -pl auth-service -am dependency:go-offline

# Copy full source now
COPY . .

# Build only auth-service
RUN mvn -pl auth-service -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

COPY --from=build /app/auth-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
