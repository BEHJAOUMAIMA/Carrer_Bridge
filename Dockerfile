FROM  maven:3.9.6-eclipse-temurin-21-jammy AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven configuration file
COPY ./pom.xml ./pom.xml

# Copy the source code
COPY ./src ./src

# Build the application
RUN mvn clean package  -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/Carrer_Bridge-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the JAR application
CMD ["java", "-jar", "app.jar"]