FROM openjdk:21
EXPOSE 8080
ADD target/spring-security-project-0.0.1-SNAPSHOT.jar securetaskmanager.jar
ENTRYPOINT ["java", "-jar", "securetaskmanager.jar"]