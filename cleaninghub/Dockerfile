# Step 1: Build the application with Maven
# Use Maven official image with JDK 17 to build the application
FROM maven:3.8.5-openjdk-17 as builder

# Set the working directory in the Docker image
WORKDIR /app

# Copy the pom.xml and source code into the Docker image
COPY pom.xml .
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Step 2: Create the final Docker image
# Use OpenJDK image to run the application
FROM openjdk:17-jdk-alpine

# Set the working directory in the Docker image
WORKDIR /app

# Copy the built jar file from the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
