package com.sperek.application;

import com.sperek.application.controller.CategoryController;
import com.sperek.application.controller.GoalController;
import com.sperek.application.controller.SessionController;
import com.sperek.application.controller.UserController;
import com.sperek.trackodoro.tracker.category.InMemoryPomodoroCategoryRepository;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngineImpl;
import com.sperek.trackodoro.tracker.session.InMemoryPomodoroSessionRepository;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngineImpl;

public class Main {

  public static void main(String[] args) {
    final InMemoryPomodoroSessionRepository sessionRepository = new InMemoryPomodoroSessionRepository();
    final InMemoryPomodoroCategoryRepository categoryRepository = new InMemoryPomodoroCategoryRepository();
    final PomodoroSessionEngineImpl sessionEngine = new PomodoroSessionEngineImpl(sessionRepository);
    final PomodoroCategoryEngineImpl categoryEngine = new PomodoroCategoryEngineImpl(categoryRepository);
    final PomodoroTracker tracker = new PomodoroTracker(sessionEngine, categoryEngine);
    final SessionController trackerController = new SessionController(tracker);
    final CategoryController categoryController = new CategoryController(tracker);
    final GoalController goalController = new GoalController(tracker);
    final UserController userController = new UserController();
    ApplicationRunner runner = new ApplicationRunner(trackerController,categoryController, goalController, userController);
    runner.run();
  }
}
