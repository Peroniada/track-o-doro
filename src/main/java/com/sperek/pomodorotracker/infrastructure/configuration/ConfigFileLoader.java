package com.sperek.pomodorotracker.infrastructure.configuration;

import java.io.File;
import java.util.Objects;

public class ConfigFileLoader {

    public File load() {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource("app-config.yml")).getFile());
    }
}
