FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY /target/hw17-0.0.1-SNAPSHOT.jar /app/app.jar
COPY app-docker.yml /app/application.yml
EXPOSE 8080
LABEL name="hw17-app"
ENTRYPOINT ["java", "-Dspring.config.location=/app/application.yml", "-jar", "/app/app.jar"]