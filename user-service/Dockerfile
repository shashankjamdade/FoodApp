FROM maven:3.9-amazoncorretto-23 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY common-libs /app/common-libs
COPY user-service /app/user-service
RUN mvn -q -DskipTests -pl user-service -am package

FROM amazoncorretto:23-alpine
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["sh","-c","java -jar /app/app.jar"]
