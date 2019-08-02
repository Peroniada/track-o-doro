package com.sperek.trackodoro;

import com.sperek.trackodoro.category.PomodoroCategory;

import java.time.ZonedDateTime;
import java.util.UUID;

public class PomodoroSession {

  private final String activityName;
  private final PomodoroCategory category;
  private final Integer duration;
  private final ZonedDateTime occurrence;
  private final UUID id;
  private final UUID ownerId;

  PomodoroSession(String activityName, PomodoroCategory category, Integer duration,
      ZonedDateTime occurrence, UUID id, UUID ownerId) {
    this.activityName = activityName;
    this.category = category;
    this.duration = duration;
    this.occurrence = occurrence;
    this.id = id;
    this.ownerId = ownerId;
  }

  public String getActivityName() {
    return activityName;
  }

  public String getCategory() {
    return category.getCategoryName();
  }

  public Integer getDuration() {
    return duration;
  }

  public ZonedDateTime getOccurrence() {
    return occurrence;
  }

  public UUID sessionsOwner() {
    return this.ownerId;
  }

  UUID getId() {
    return id;
  }
}
