FROM amazoncorretto:17.0.5 as rabbitmq
ARG JAR_FILE=target/collabo-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/collabo-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","--add-opens","java.base/java.lang=ALL-UNNAMED","-jar","/app/collabo-service-0.0.1-SNAPSHOT.jar"]