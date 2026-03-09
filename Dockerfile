FROM openjdk:21-jdk-slim AS build
MAINTAINER alanjhone@gmail.com

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew build -x test -x testClasses --no-daemon

FROM openjdk:21-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/softdesign-api-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/softdesign-api-0.0.1-SNAPSHOT.jar"]
