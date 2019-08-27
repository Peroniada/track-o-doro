package com.sperek.application;

import com.sperek.application.controller.CategoryController;
import com.sperek.application.controller.GoalController;
import com.sperek.application.controller.SessionController;
import com.sperek.application.controller.UserController;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.category.InMemoryPomodoroCategoryRepository;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngine;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngineImpl;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryRepository;
import com.sperek.trackodoro.tracker.session.InMemoryPomodoroSessionRepository;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngine;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngineImpl;
import com.sperek.trackodoro.tracker.session.PomodoroSessionRepository;
import java.util.UUID;

public class Main {

  public static void main(String[] args) {
    final PomodoroSessionRepository sessionRepository = new InMemoryPomodoroSessionRepository();
    final PomodoroCategoryRepository categoryRepository = new InMemoryPomodoroCategoryRepository();
    final PomodoroSessionEngine<UUID> sessionEngine = new PomodoroSessionEngineImpl(sessionRepository);
    final PomodoroCategoryEngine categoryEngine = new PomodoroCategoryEngineImpl(categoryRepository);
    final PomodoroTracker tracker = new PomodoroTracker(sessionEngine, categoryEngine);
    final SessionController sessionController = new SessionController(tracker);
    final CategoryController categoryController = new CategoryController(tracker);
    final GoalController goalController = new GoalController(tracker);
    final UserController userController = new UserController();
    final JavalinConfig javalinConfig = new JavalinConfig(sessionController, categoryController, goalController, userController);
    final JsonMapperConfig jsonMapperConfig = new JsonMapperConfig();
    final ApplicationRunner runner = new ApplicationRunner(javalinConfig, jsonMapperConfig);
    runner.run();
  }
}
