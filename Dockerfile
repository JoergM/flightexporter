FROM openjdk:8-alpine

COPY build/libs/flightexporter-1.0-SNAPSHOT.jar flightexporter-1.0-SNAPSHOT.jar
EXPOSE 9555

CMD java -jar flightexporter-1.0-SNAPSHOT.jar
