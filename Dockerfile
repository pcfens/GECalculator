FROM maven:3.6-jdk-11 AS build

COPY . /app
WORKDIR /app

RUN mvn package

FROM tomcat:9-jdk11

RUN rm -Rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps