package com.sperek.pomodorotracker.domain.sessionFilter;

import com.sperek.pomodorotracker.domain.sessionFilter.composite.spec.Specification;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSession;

public class CategorySpecification implements Specification<PomodoroSession> {

  private final String requiredCategory;

  public CategorySpecification(String requiredCategory) {
    this.requiredCategory = requiredCategory;
  }

  @Override
  public boolean isSatisfiedBy(PomodoroSession pomodoroSession) {
    String sessionCategory = pomodoroSession.sessionCategoryName();
    return requiredCategory.equals(sessionCategory);
  }
}
