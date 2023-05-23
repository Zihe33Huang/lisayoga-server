FROM openjdk:11-jdk-slim
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE prod
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]