FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Copy the built JAR to a standard location
RUN mkdir -p /app/target
# Adjust the JAR name if needed
RUN cp target/*.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Run the JAR directly
ENTRYPOINT ["java", "-jar", "app.jar"]