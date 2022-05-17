FROM eclipse-temurin:11-jre-alpine
COPY target/data-generator-api-*-runner.jar app.jar

ENTRYPOINT ["java", "-Dopen-browser=false", "-jar", "/app.jar"]