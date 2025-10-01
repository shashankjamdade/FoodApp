# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent + required POMs
COPY pom.xml .
COPY common-libs/pom.xml common-libs/
COPY gateway/pom.xml gateway/

# Fetch only gateway + common-libs dependencies
RUN mvn -pl gateway -am dependency:go-offline

# Copy full source
COPY . .

# Build gateway only
RUN mvn -pl gateway -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/gateway/target/app.jar app.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]
