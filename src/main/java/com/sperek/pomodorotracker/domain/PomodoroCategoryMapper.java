package com.sperek.pomodorotracker.domain;

import com.sperek.pomodorotracker.domain.model.PomodoroCategory;
import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import com.sperek.pomodorotracker.domain.tracker.dto.PomodoroCategoryDTO;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PomodoroCategoryMapper {

  public static Function<PomodoroCategory, PomodoroCategoryDTO> toDto = (pomodoroCategory ->
      PomodoroCategoryDTO.builder()
          .categoryName(pomodoroCategory.getCategoryName())
          .dailyGoal(pomodoroCategory.sessionsToCompleteDailyGoal())
          .weeklyGoal(pomodoroCategory.sessionsToCompleteWeeklyGoal())
          .build()
  );

  public static BiFunction<PomodoroCategoryDTO, UUID, PomodoroCategory> fromDto = (pomodoroCategoryDTO, ownerId) -> {
    return new PomodoroCategory(
        pomodoroCategoryDTO.getCategoryName(),
        new DailyGoal(pomodoroCategoryDTO.getDailyGoal()),
        new WeeklyGoal(pomodoroCategoryDTO.getWeeklyGoal()),
        ownerId);
  };

}
