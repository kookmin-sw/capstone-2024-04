# First stage: build the JAR file
FROM openjdk:17-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Second stage: copy the JAR file from the builder stage
FROM openjdk:17-alpine

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

