package com.sperek.trackodoro;

import com.sperek.trackodoro.tracker.session.PomodoroSession;
import com.sperek.trackodoro.tracker.session.PomodoroSessionBuilder;
import com.sperek.trackodoro.tracker.dto.PomodoroSessionDTO;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PomodoroSessionMapper {

  public static Function<PomodoroSession, PomodoroSessionDTO> toDto = (pomodoroSession -> PomodoroSessionDTO
      .builder()
      .activityName(pomodoroSession.getActivityName())
      .category(pomodoroSession.sessionCategoryName())
      .duration(pomodoroSession.getDuration())
      .occurrence(pomodoroSession.getOccurrence())
      .id(pomodoroSession.getId())
      .build());


  public static BiFunction<PomodoroSessionDTO, UUID, PomodoroSession> fromDto = (pomodoroSessionDTO, ownerId) -> PomodoroSessionBuilder
      .aPomodoroSession()
      .withActivityName(pomodoroSessionDTO.getActivityName())
      .withCategory(pomodoroSessionDTO.getCategory())
      .withDuration(pomodoroSessionDTO.getDuration())
      .withOccurrence(pomodoroSessionDTO.getOccurrence())
      .withId(pomodoroSessionDTO.getId())
      .withOwnerId(ownerId)
      .build();
}
