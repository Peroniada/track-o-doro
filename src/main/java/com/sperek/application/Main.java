package com.sperek.application;

import com.sperek.application.controller.category.CategoryController;
import com.sperek.application.controller.GoalController;
import com.sperek.application.controller.session.SessionController;
import com.sperek.application.controller.user.UserController;
import com.sperek.application.token.JWTTokenizer;
import com.sperek.application.token.Tokenizer;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.category.InMemoryPomodoroCategoryRepository;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngine;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngineImpl;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryRepository;
import com.sperek.trackodoro.tracker.session.InMemoryPomodoroSessionRepository;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngine;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngineImpl;
import com.sperek.trackodoro.tracker.session.PomodoroSessionRepository;
import com.sperek.trackodoro.user.InMemoryUserRepository;
import com.sperek.trackodoro.user.PBKDF2PasswordEncryptor;
import com.sperek.trackodoro.user.UserSystem;
import java.util.UUID;

public class Main {

  public static void main(String[] args) {
    final PomodoroSessionRepository sessionRepository = new InMemoryPomodoroSessionRepository();
    final PomodoroSessionEngine<UUID> sessionEngine = new PomodoroSessionEngineImpl(sessionRepository);

    final PomodoroCategoryRepository categoryRepository = new InMemoryPomodoroCategoryRepository();
    final PomodoroCategoryEngine categoryEngine = new PomodoroCategoryEngineImpl(categoryRepository);

    final PomodoroTracker tracker = new PomodoroTracker(sessionEngine, categoryEngine);
    final Tokenizer tokenizer = new JWTTokenizer();
    final SessionController sessionController = new SessionController(tracker, tokenizer);
    final CategoryController categoryController = new CategoryController(tracker, tokenizer);
    final GoalController goalController = new GoalController(tracker);

    final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    final PBKDF2PasswordEncryptor passwordEncryptor = new PBKDF2PasswordEncryptor();
    final UserSystem userSystem = new UserSystem(userRepository, passwordEncryptor);
    final JWTTokenizer tokenGenerator = new JWTTokenizer();

    final UserController userController = new UserController(userSystem, tokenGenerator);

    final JavalinConfig javalinConfig = new JavalinConfig(sessionController, categoryController, goalController, userController, tokenizer);
    final JsonMapperConfig jsonMapperConfig = new JsonMapperConfig();
    final ApplicationRunner runner = new ApplicationRunner(javalinConfig, jsonMapperConfig);
    runner.run();
  }
}
