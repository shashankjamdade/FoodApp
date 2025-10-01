# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent pom + service pom (to cache dependencies)
COPY pom.xml .
COPY common-libs/pom.xml common-libs/
COPY auth-service/pom.xml auth-service/

# Download dependencies (cached layer)
RUN mvn -B dependency:go-offline

# Copy all source code
COPY . .

# Build only auth-service module and its dependencies
RUN mvn -pl auth-service -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
