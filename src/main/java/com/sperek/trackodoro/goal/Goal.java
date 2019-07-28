package com.sperek.trackodoro.goal;

public interface Goal<ID> {
  Integer getNumberOfSessionsToFulfill();
  ID getGoalId();
}
