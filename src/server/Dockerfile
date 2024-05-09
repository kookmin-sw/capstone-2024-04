FROM openjdk:17-alpine
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

ARG JAR_FILE=./build/libs/server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]