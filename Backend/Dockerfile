# Use official OpenJDK image from Docker Hub
FROM openjdk:17-jdk-slim

# Install Maven (for building the application)
RUN apt-get update && apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files into the container
COPY pom.xml /app/pom.xml
COPY src /app/src

# Build the application using Maven
RUN mvn clean package -DskipTests

# Copy the built WAR file into the container
COPY target/Expense-Tracker-0.0.1-SNAPSHOT.war /app/Expense-Tracker-0.0.1-SNAPSHOT.war

# Expose the port that the Spring Boot app will run on (default port 8080)
EXPOSE 8080

# Command to run the Spring Boot application with the WAR file
ENTRYPOINT ["java", "-jar", "/app/Expense-Tracker-0.0.1-SNAPSHOT.war"]
