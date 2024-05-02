FROM openjdk:17
ARG JAR_FILE=target/tingeso1-backend.jar
COPY ${JAR_FILE} tingeso1-backend.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/tingeso1-backend.jar"]