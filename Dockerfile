FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./java-maven-build/target/java-maven-app 1.1.0-SNAPSHOT /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "java-maven-app 1.1.0-SNAPSHOT.jar"]