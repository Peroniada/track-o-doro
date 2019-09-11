package com.sperek.trackodoro.tracker.dto;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PomodoroSessionDTO {

  public String activityName;
  public String category;
  public Integer duration;
  public ZonedDateTime occurrence;
  public UUID id;

}
