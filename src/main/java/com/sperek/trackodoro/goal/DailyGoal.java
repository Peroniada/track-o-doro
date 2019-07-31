package com.sperek.trackodoro.goal;

public class DailyGoal implements Goal{

  private Integer numberOfSessionsToFulfill;

  public DailyGoal(Integer numberOfSessionsToFulfill) {
    this.numberOfSessionsToFulfill = numberOfSessionsToFulfill;
  }

  @Override
  public Integer getNumberOfSessionsToFulfill() {
    return this.numberOfSessionsToFulfill;
  }

}
