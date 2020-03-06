package com.sperek.pomodorotracker.infrastructure.configuration;

import com.sperek.pomodorotracker.application.JooqConfig;

public class AppConfiguration {
    private JooqConfig database;

    public JooqConfig getJooqConfig() {
        return database;
    }
}


