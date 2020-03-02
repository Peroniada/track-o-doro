FROM openjdk:8
COPY . /usr/app
WORKDIR /usr/app
EXPOSE 8891
RUN chmod +x ./gradlew && ./gradlew flywayMigrate
RUN ./gradlew generateSampleJooqSchemaSource && ./gradlew fatJar
CMD ["java", "-jar", "./build/libs/track-o-doro-1.0-SNAPSHOT-all.jar"]
