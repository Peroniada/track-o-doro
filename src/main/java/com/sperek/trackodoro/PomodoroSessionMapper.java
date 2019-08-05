package com.sperek.trackodoro;

import com.sperek.trackodoro.tracker.session.PomodoroSession;
import com.sperek.trackodoro.tracker.session.PomodoroSessionBuilder;
import com.sperek.trackodoro.tracker.dto.PomodoroSessionDTO;
import java.util.function.Function;

public class PomodoroSessionMapper {

  public static Function<PomodoroSession, PomodoroSessionDTO> toDto = (pomodoroSession -> PomodoroSessionDTO
      .builder()
      .activityName(pomodoroSession.getActivityName())
      .category(pomodoroSession.sessionCategoryName())
      .duration(pomodoroSession.getDuration())
      .occurrence(pomodoroSession.getOccurrence())
      .id(pomodoroSession.getId())
      .ownerId(pomodoroSession.sessionsOwner())
      .build());


  public static Function<PomodoroSessionDTO, PomodoroSession> fromDto = (pomodoroSessionDTO -> PomodoroSessionBuilder
      .aPomodoroSession()
      .withActivityName(pomodoroSessionDTO.getActivityName())
      .withCategory(pomodoroSessionDTO.getCategory())
      .withDuration(pomodoroSessionDTO.getDuration())
      .withOccurrence(pomodoroSessionDTO.getOccurrence())
      .withId(pomodoroSessionDTO.getId())
      .withOwnerId(pomodoroSessionDTO.getOwnerId())
      .build());
}
