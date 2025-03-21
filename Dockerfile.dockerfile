# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN apt-get update && apt-get install -y maven
RUN mvn clean package

# Expose the port your app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/your-app.jar"]
