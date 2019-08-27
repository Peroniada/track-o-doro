package com.sperek.trackodoro;

import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import com.sperek.trackodoro.tracker.dto.PomodoroCategoryDTO;
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
