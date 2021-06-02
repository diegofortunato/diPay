FROM adoptopenjdk/openjdk11:latest
MAINTAINER Diego Fortunato
COPY build/libs/diPay-0.0.1-SNAPSHOT.jar dipay-0.0.1.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/dipay-0.0.1.jar"]
