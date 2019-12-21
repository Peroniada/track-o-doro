package com.sperek.pomodorotracker.domain.tracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PomodoroCategoryDTO {
  private String categoryName;
  private Integer dailyGoal;
  private Integer weeklyGoal;
  }
