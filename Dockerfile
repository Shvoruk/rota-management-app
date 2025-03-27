# Dockerfile

# 1. Build Stage
FROM maven:3.9.9-amazoncorretto-21-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copy only the pom.xml first to take advantage of cached dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Now copy the rest of the source code
COPY src/ ./src/

# Package the application (skip tests here)
RUN mvn clean package -DskipTests

# 2. Runtime Stage
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

# Start application when container is run
ENTRYPOINT ["java", "-jar", "/app/app.jar"]