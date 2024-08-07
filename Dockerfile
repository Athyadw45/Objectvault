# Stage 1: Build the application
FROM eclipse-temurin:21_35-jdk-alpine as builder

# Set the working directory
WORKDIR /app

# Copy the build.gradle file and the gradle folder
COPY build.gradle /app/
COPY gradle /app/gradle
COPY gradlew /app/gradlew

# Copy the rest of the application source code
COPY src /app/src

# Install dependencies and compile the application
RUN ./gradlew build --no-daemon

# Stage 2: Create the final image
FROM eclipse-temurin:21_35-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
