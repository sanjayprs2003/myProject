FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY src /app/src

RUN mvn clean package -DskipTests

COPY target/Expense-Tracker-0.0.1-SNAPSHOT.war /app/Expense-Tracker-0.0.1-SNAPSHOT.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/Expense-Tracker-0.0.1-SNAPSHOT.war"]
