# Use lightweight Eclipse Temurin Java 17 Alpine runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install curl and bash for utility and health checks
RUN apk add --no-cache curl bash

# Copy prebuilt executable Spring Boot JARs for all three microservices
COPY student-service/target/student-service.jar /app/student-service.jar
COPY course-service/target/course-service.jar /app/course-service.jar
COPY result-service/target/result-service.jar /app/result-service.jar

# Copy and configure the startup process supervisor script
COPY docker-start.sh /app/docker-start.sh
RUN sed -i 's/\r$//' /app/docker-start.sh && chmod +x /app/docker-start.sh

# Expose ports for all 3 microservices
# 8081: Student Service
# 8082: Course Service
# 8083: Result Service
EXPOSE 8081 8082 8083

# Launch all three microservices simultaneously via the supervisor script
ENTRYPOINT ["/app/docker-start.sh"]
