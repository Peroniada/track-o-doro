package com.sperek.pomodorotracker.application.api;

public class PomodoroApi {
  private UserController userController;
  private CategoryController categoryController;
  private GoalController goalController;
  private PomodoroController pomodoroController;

  public PomodoroApi(
      UserController userController,
      CategoryController categoryController,
      GoalController goalController,
      PomodoroController pomodoroController) {
    this.userController = userController;
    this.categoryController = categoryController;
    this.goalController = goalController;
    this.pomodoroController = pomodoroController;
  }

    public UserController getUserController() {
        return userController;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public GoalController getGoalController() {
        return goalController;
    }

    public PomodoroController getPomodoroController() {
        return pomodoroController;
    }
}
