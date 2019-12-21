package com.sperek.pomodorotracker.domain.tracker.category;

import com.sperek.pomodorotracker.application.ports.secondary.PomodoroCategoryRepository;
import com.sperek.pomodorotracker.domain.model.PomodoroCategory;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class InMemoryPomodoroCategoryRepository implements PomodoroCategoryRepository {

  private final Map<UUID, Set<PomodoroCategory>> categories = new HashMap<>();

  @Override
  public PomodoroCategory findByName(UUID ownerId, String name) {
    return categories.get(ownerId).stream()
        .filter(category -> category.getCategoryName().equals(name)).findFirst()
        .orElseThrow(() -> new RuntimeException("No category found"));
  }

  @Override
  public void save(PomodoroCategory pomodoroCategory) {
    Set<PomodoroCategory> newSetWithCategory = new HashSet<>();
    newSetWithCategory.add(pomodoroCategory);
    categories.putIfAbsent(pomodoroCategory.getOwnerId(), newSetWithCategory);
    categories.get(pomodoroCategory.getOwnerId()).add(pomodoroCategory);
  }

  @Override
  public Collection<PomodoroCategory> findAll(UUID ownerId) {
    return categories.get(ownerId);
  }

  @Override
  public void delete(UUID ownerId, String category) {
    categories.get(ownerId).removeIf(cat -> cat.getCategoryName().equals(category));
  }
}
