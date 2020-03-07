package com.sperek.pomodorotracker.application;

import com.sperek.pomodorotracker.application.api.CategoryController;
import com.sperek.pomodorotracker.application.api.GoalController;
import com.sperek.pomodorotracker.application.api.SessionController;
import com.sperek.pomodorotracker.application.api.UserController;
import com.sperek.pomodorotracker.application.ports.PomodoroCategoryEngine;
import com.sperek.pomodorotracker.application.ports.PomodoroSessionEngine;
import com.sperek.pomodorotracker.application.ports.secondary.PomodoroCategoryRepository;
import com.sperek.pomodorotracker.application.ports.secondary.PomodoroSessionRepository;
import com.sperek.pomodorotracker.application.ports.secondary.UserRepository;
import com.sperek.pomodorotracker.application.security.JWTTokenizer;
import com.sperek.pomodorotracker.application.serialization.JsonMapperConfig;
import com.sperek.pomodorotracker.domain.tracker.PomodoroTracker;
import com.sperek.pomodorotracker.domain.tracker.category.InMemoryPomodoroCategoryRepository;
import com.sperek.pomodorotracker.domain.tracker.category.PomodoroCategoryEngineImpl;
import com.sperek.pomodorotracker.domain.tracker.session.InMemoryPomodoroSessionRepository;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSessionEngineImpl;
import com.sperek.pomodorotracker.domain.user.PBKDF2PasswordEncryptor;
import com.sperek.pomodorotracker.domain.user.UserService;
import com.sperek.pomodorotracker.infrastructure.configuration.AppConfiguration;
import com.sperek.pomodorotracker.infrastructure.configuration.ConfigurationLoader;
import com.sperek.pomodorotracker.infrastructure.persistence.JooqUserRepository;

import java.io.File;
import java.util.UUID;

public class ApplicationRunner implements Runnable {

  @Override
  public void run() {
    final PomodoroSessionRepository sessionRepository = new InMemoryPomodoroSessionRepository();
    final PomodoroSessionEngine<UUID> sessionEngine = new PomodoroSessionEngineImpl(sessionRepository);

    final ConfigurationLoader configurationLoader = new ConfigurationLoader();

    ClassLoader classLoader = getClass().getClassLoader();
    final File config = new File(classLoader.getResource("db-config.yml").getFile());
    final AppConfiguration appConfiguration = configurationLoader.loadConfiguration(
        config, AppConfiguration.class);

    final PomodoroCategoryRepository categoryRepository = new InMemoryPomodoroCategoryRepository();
    final PomodoroCategoryEngine categoryEngine = new PomodoroCategoryEngineImpl(categoryRepository);

    final PomodoroTracker tracker = new PomodoroTracker(sessionEngine, categoryEngine);
    final JWTTokenizer tokenizer = new JWTTokenizer();
    final SessionController sessionController = new SessionController(tracker, tokenizer);
    final CategoryController categoryController = new CategoryController(tracker, tokenizer);
    final GoalController goalController = new GoalController(tracker);

    final UserRepository userRepository = new JooqUserRepository(appConfiguration.getJooqConfig());
    final PBKDF2PasswordEncryptor passwordEncryptor = new PBKDF2PasswordEncryptor();
    final UserService userService = new UserService(userRepository, passwordEncryptor);

    final UserController userController = new UserController(userService, tokenizer);

    final JavalinConfig javalinConfig = new JavalinConfig(sessionController, categoryController, goalController, userController, tokenizer);
    final JsonMapperConfig jsonMapperConfig = new JsonMapperConfig();

    javalinConfig.run();
    jsonMapperConfig.run();
  }

}
