FROM openjdk:18
WORKDIR /app

COPY ./target/user-management-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "user-management-0.0.1-SNAPSHOT.jar"]