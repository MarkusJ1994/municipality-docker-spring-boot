FROM openjdk:17-jdk

EXPOSE 8080

ARG WAR_FILE=build/libs/*.war
COPY ${WAR_FILE} app.war
ENTRYPOINT ["java", "-jar", "/app.war"]