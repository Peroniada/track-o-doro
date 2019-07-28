package com.sperek.trackodoro.goal;

import java.util.HashMap;
import java.util.Map;

public class InMemoryGoalRepository<T extends Goal, ID> implements GoalRepository <T,ID> {

  private final Map<ID, T> goals = new HashMap<>();

  @Override
  public T save(T goal) {
    return this.goals.put((ID) goal.getGoalId(), goal);
  }

  @Override
  public Iterable<T> findAll() {
    return this.goals.values();
  }

  @Override
  public T getOne(ID id) {
    return this.goals.get(id);
  }
}
