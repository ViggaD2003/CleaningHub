FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app
COPY . .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/cleaninghub-0.0.1-SNAPSHOT.jar app.jar

# Expose the port and set the entry point
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]