package com.sperek.trackodoro.goal;

import java.util.UUID;

public class WeeklyGoal implements Goal{

  private Integer numberOfSessions;
  private UUID ownerId;

  public WeeklyGoal(Integer numberOfSessions, UUID ownerId) {
    this.numberOfSessions = numberOfSessions;
    this.ownerId = ownerId;
  }

  public Integer getNumberOfSessionsToFulfill() {
    return numberOfSessions;
  }

  @Override
  public UUID getGoalId() {
    return this.ownerId;
  }

}
