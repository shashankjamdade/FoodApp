# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy pom files first to cache dependencies
COPY pom.xml .
COPY user-service/pom.xml user-service/
COPY common-libs/pom.xml common-libs/

# Download dependencies (faster builds, cached layer)
RUN mvn -B dependency:go-offline

# Copy source code
COPY . .

# Build only user-service module
RUN mvn -pl user-service -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

# Copy user-service jar from build stage
COPY --from=build /app/user-service/target/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
