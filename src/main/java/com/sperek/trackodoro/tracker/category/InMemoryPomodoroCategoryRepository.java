package com.sperek.trackodoro.tracker.category;

import com.sperek.trackodoro.category.PomodoroCategory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPomodoroCategoryRepository implements PomodoroCategoryRepository {

  private final Map<String, PomodoroCategory> categories = new HashMap<>();

  @Override
  public PomodoroCategory findByName(String name) {
    return Optional.ofNullable(categories.get(name)).orElseThrow(() -> new RuntimeException("Category not found"));
  }

  @Override
  public void save(PomodoroCategory pomodoroCategory) {
    categories.put(pomodoroCategory.getCategoryName(), pomodoroCategory);
  }

  @Override
  public Collection<PomodoroCategory> findAll() {
    return categories.values();
  }

  @Override
  public void delete(String category) {
    categories.remove(category);
  }
}
