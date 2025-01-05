FROM openjdk:17-jdk-alpine
WORKDIR /opt
ENV PORT=8088
EXPOSE 8088
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar