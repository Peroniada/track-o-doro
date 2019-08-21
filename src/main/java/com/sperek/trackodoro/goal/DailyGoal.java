package com.sperek.trackodoro.goal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
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
