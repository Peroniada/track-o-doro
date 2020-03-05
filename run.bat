./gradlew flywayMigrate
./gradlew generateSampleJooqSchemaSource
./gradlew fatJar
java -jar ./build/libs/track-o-doro-1.0-SNAPSHOT-all.jar