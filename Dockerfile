FROM openjdk:11

LABEL maintainer="roundtable"

COPY . /ghaction
WORKDIR /ghaction

RUN ./gradlew wrapper --gradle-version 7.4.2

EXPOSE 8080

CMD ["java", "-jar", "/ghaction/build/libs/ghaction-0.0.1-SNAPSHOT.jar"]