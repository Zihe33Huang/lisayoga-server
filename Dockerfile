FROM maven:3.8.1-openjdk-11

COPY . /app
WORKDIR /app

ENTRYPOINT ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=prod"]