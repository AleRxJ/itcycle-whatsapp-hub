# Multi-stage Dockerfile for iTCycle WhatsApp Hub

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom files first (for better caching)
COPY pom.xml .
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY adapters-in-web/pom.xml adapters-in-web/
COPY adapters-out-persistence/pom.xml adapters-out-persistence/
COPY adapters-out-external/pom.xml adapters-out-external/
COPY bootstrap/pom.xml bootstrap/

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY domain/src domain/src
COPY application/src application/src
COPY adapters-in-web/src adapters-in-web/src
COPY adapters-out-persistence/src adapters-out-persistence/src
COPY adapters-out-external/src adapters-out-external/src
COPY bootstrap/src bootstrap/src

# Build application
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=build /app/bootstrap/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]
