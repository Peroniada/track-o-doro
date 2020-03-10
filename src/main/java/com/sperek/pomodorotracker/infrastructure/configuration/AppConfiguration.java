package com.sperek.pomodorotracker.infrastructure.configuration;

public class AppConfiguration {
    private DatabaseConfig databaseConfig;

    private HttpConfig httpConfig;

    public DatabaseConfig getJooqConfig() {
        return databaseConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }
}


