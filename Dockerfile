FROM eclipse-temurin:21-jdk-alpine AS build
MAINTAINER alanjhone@gmail.com

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew build -x test -x testClasses --no-daemon

FROM eclipse-temurin:21-jre-alpine

RUN mkdir /app

# Cria usuário não-root e define como padrão
RUN adduser -S appuser && chown -R appuser /app
USER appuser

COPY --from=build /home/gradle/src/build/libs/desafio-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/desafio-0.0.1-SNAPSHOT.jar"]
