FROM openjdk:17
ARG JAR_FILE=target/tingeso_pep_1-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} tingeso_pep_1-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/tingeso_pep_1-0.0.1-SNAPSHOT.jar"]