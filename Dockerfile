FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
COPY serviceAccountKey.json /serviceAccountKey.json
ENV GOOGLE_APPLICATION_CREDENTIALS="/serviceAccountKey.json"
ENTRYPOINT ["java","-jar","/app.jar"]