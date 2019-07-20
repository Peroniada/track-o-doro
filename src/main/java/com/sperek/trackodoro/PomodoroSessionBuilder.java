package com.sperek.trackodoro;

import java.time.ZonedDateTime;
import java.util.UUID;

public final class PomodoroSessionBuilder {

  private String activityName;
  private String category;
  private Integer duration;
  private ZonedDateTime occurrence;
  private UUID id;

  private PomodoroSessionBuilder() {
  }

  public static PomodoroSessionBuilder aPomodoroSession() {
    return new PomodoroSessionBuilder();
  }

  public PomodoroSessionBuilder withActivityName(String activityName) {
    this.activityName = activityName;
    return this;
  }

  public PomodoroSessionBuilder withCategory(String category) {
    this.category = category;
    return this;
  }

  public PomodoroSessionBuilder withDuration(Integer duration) {
    this.duration = duration;
    return this;
  }

  public PomodoroSessionBuilder withOccurrence(ZonedDateTime occurrence) {
    this.occurrence = occurrence;
    return this;
  }

  public PomodoroSessionBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public PomodoroSession build() {
    return new PomodoroSession(activityName, category, duration, occurrence, id);
  }
}
