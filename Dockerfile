FROM maven:3.8.2-openjdk-8-slim as builder

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:resolve

COPY src src
RUN mvn package -DskipTests

FROM gcr.io/spark-operator/spark:v3.1.1-hadoop3

COPY --from=builder /app/target/ked-speakers-results-0.0.1-SNAPSHOT-shaded.jar ked-speakers-results-0.0.1-SNAPSHOT-shaded.jar
