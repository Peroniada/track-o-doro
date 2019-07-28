package com.sperek.trackodoro.goal;

public interface GoalRepository<T extends Goal, ID> {
  T save(T goal);
  Iterable<T> findAll();
  T getOne(ID id);
}
