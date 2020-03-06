package com.sperek.pomodorotracker.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public class ConfigurationLoader {
    private final ObjectMapper objectMapper;
    private final StringSubstitutor stringSubstitutor;

    public ConfigurationLoader() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.stringSubstitutor = new StringSubstitutor(StringLookupFactory.INSTANCE.environmentVariableStringLookup());
    }

    public <T> T loadConfiguration(File config, Class<T> clazz) {
        try {
            String contents =
                    this.stringSubstitutor.replace(new String(Files.readAllBytes(config.toPath())));

            return this.objectMapper.readValue(contents, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
