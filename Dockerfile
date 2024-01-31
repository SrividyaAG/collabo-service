FROM amazoncorretto:17.0.5
ARG JAR_FILE=target/collabo-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/collabo-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/collabo-service-0.0.1-SNAPSHOT.jar"]