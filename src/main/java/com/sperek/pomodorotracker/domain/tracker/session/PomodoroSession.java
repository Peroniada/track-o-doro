package com.sperek.pomodorotracker.domain.tracker.session;

import java.time.ZonedDateTime;
import java.util.UUID;

public class PomodoroSession {

  private final String activityName;
  private final String category;
  private final Integer duration;
  private final ZonedDateTime occurrence;
  private final UUID id;
  private final UUID ownerId;

  public PomodoroSession(String activityName, String category, Integer duration,
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

  public String sessionCategoryName() {
    return category;
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

  public UUID getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public UUID getOwnerId() {
    return ownerId;
  }
}
