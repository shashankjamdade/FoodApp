# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent + required POMs

# Copy parent + all module pom.xmls (needed for parent resolution)
COPY pom.xml .
COPY common-libs/pom.xml common-libs/
COPY auth-service/pom.xml auth-service/
COPY user-service/pom.xml user-service/
COPY gateway/pom.xml gateway/
# Fetch only auth-service + common-libs dependencies
RUN mvn -pl auth-service -am dependency:go-offline

# Copy full source
COPY . .

# Build auth-service only
RUN mvn -pl auth-service -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/auth-service/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
