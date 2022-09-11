FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} smart-analytics.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/smart-analytics.jar"]
