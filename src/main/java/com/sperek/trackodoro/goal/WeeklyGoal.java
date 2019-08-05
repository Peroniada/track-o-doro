package com.sperek.trackodoro.goal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
public class WeeklyGoal implements Goal {

  private Integer numberOfSessionsToFulfill;

  public WeeklyGoal(Integer numberOfSessionsToFulfill) {
    this.numberOfSessionsToFulfill = numberOfSessionsToFulfill;
  }

  public Integer getNumberOfSessionsToFulfill() {
    return numberOfSessionsToFulfill;
  }
}
