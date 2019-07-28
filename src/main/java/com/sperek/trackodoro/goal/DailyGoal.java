package com.sperek.trackodoro.goal;

import java.util.UUID;

public class DailyGoal implements Goal <UUID>{

  private UUID ownerId;
  private Integer numberOfSessionsToFulfill;

  public DailyGoal(UUID ownerId, Integer numberOfSessionsToFulfill) {
    this.ownerId = ownerId;
    this.numberOfSessionsToFulfill = numberOfSessionsToFulfill;
  }

  @Override
  public Integer getNumberOfSessionsToFulfill() {
    return this.numberOfSessionsToFulfill;
  }

  @Override
  public UUID getGoalId() {
    return ownerId;
  }

}
