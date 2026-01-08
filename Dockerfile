FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy only what we need to build
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
COPY src src

RUN chmod +x mvnw && ./mvnw -DskipTests package -DskipITs

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy fat jar produced by build stage
COPY --from=build /workspace/target/quickswap-backend-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""
EXPOSE 8080

# If FIREBASE_SERVICE_ACCOUNT is set, write it to a file and set GOOGLE_APPLICATION_CREDENTIALS
CMD ["sh", "-c", "if [ -n \"$FIREBASE_SERVICE_ACCOUNT\" ]; then echo \"$FIREBASE_SERVICE_ACCOUNT\" > /app/serviceAccountKey.json; export GOOGLE_APPLICATION_CREDENTIALS=/app/serviceAccountKey.json; fi && java $JAVA_OPTS -jar /app/app.jar"]
