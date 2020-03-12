package com.sperek.pomodorotracker.infrastructure.configuration;

public class AppConfiguration {

    private DatabaseConfig database;

    private HttpConfig http;

    public DatabaseConfig getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }

    public HttpConfig getHttp() {
        return http;
    }

    public void setHttp(HttpConfig http) {
        this.http = http;
    }
}


