FROM openjdk:11
ARG VERSION

RUN ["/bin/sh", "-c", ": ${VERSION:?Artifact version must be specified}"]
#RUN if [ -z ${VERSION} ]; then echo 'Environment variable VERSION must be specified. Exiting.'; exit 1; fi

ARG JAR_FILE=build/libs/java-assessment-base-${VERSION}.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

CMD ["-Xms512M"]