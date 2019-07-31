package com.sperek.trackodoro.goal;

public class WeeklyGoal implements Goal {

  private Integer numberOfSessionsToFulfill;

  public WeeklyGoal(Integer numberOfSessionsToFulfill) {
    this.numberOfSessionsToFulfill = numberOfSessionsToFulfill;
  }

  public Integer getNumberOfSessionsToFulfill() {
    return numberOfSessionsToFulfill;
  }
}
