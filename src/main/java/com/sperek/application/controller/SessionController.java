package com.sperek.application.controller;

import com.sperek.application.controller.query.QueryResolver;
import com.sperek.trackodoro.PomodoroSessionMapper;
import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.dto.PomodoroSessionDTO;
import com.sperek.trackodoro.tracker.session.PomodoroSession;
import io.javalin.http.Handler;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionController {

  private PomodoroTracker tracker;
  private QueryResolver queryResolver;
  private Logger log = LoggerFactory.getLogger(SessionController.class);
  public SessionController(PomodoroTracker tracker) {
    this.tracker = tracker;
    this.queryResolver = new QueryResolver();
  }


  public Handler saveSession = ctx -> {
    log.info(ctx.body());
    final PomodoroSessionDTO session = ctx.bodyAsClass(PomodoroSessionDTO.class);

    tracker.saveSession(PomodoroSessionMapper.fromDto.apply(session));
    ctx.status(201);
  };
  Handler saveSessions(Collection<PomodoroSessionDTO> sessions) {
    return null;
  }


  public Handler allUserSessions = ctx -> {
    final UUID ownerId = UUID.fromString(Objects.requireNonNull(ctx.queryParam("ownerId")));
    final Collection<PomodoroSession> sessions = tracker.allUserSessions(ownerId);
    ctx.json(sessionsToDTO(sessions));
    ctx.status(200);
  };

  public Handler sessionsByCategory = ctx -> {
    String category = ctx.queryParam("categoryName");

    ctx.json(sessionsToDTO(tracker.findSatisfyingSessions(new CategorySpecification(category))));
    ctx.status(200);
  };


  public Handler countSessionsByCategory = ctx -> {
    String category = ctx.queryParam("categoryName");
    ctx.json(tracker.countSessionsByCategory(category));
    ctx.status(200);
  };

  public Handler countSessions = ctx -> {
    Specification<PomodoroSession> sessionSpecification = queryResolver.resolve(ctx.queryParamMap());
    ctx.json(tracker.countSessions(sessionSpecification));
  };
  public Handler countSessionsByDay = ctx -> {
    LocalDate occurrence = occurrenceFrom(ctx.queryParam("date"));
    ctx.json(tracker.countSessionsByDay(occurrence));
    ctx.status(200);
  };

  public Handler countSessionsByDateAndCategory = ctx -> {
    LocalDate occurrence = occurrenceFrom(ctx.queryParam("date"));
    final String categoryName = ctx.queryParam("categoryName");
    ctx.json(tracker.countSessionsByDateAndCategory(occurrence, categoryName));
    ctx.status(200);
  };

  public Handler getSession = ctx ->  {
    tracker.getSession(UUID.fromString(ctx.pathParam("id")));
  };

  public Handler sessionsSummaryForUser = ctx ->  {
    UUID ownerId = UUID.fromString(Optional.ofNullable(ctx.header("userId")).orElseThrow(() -> new RuntimeException("No userId provided")));
    ctx.json(tracker.sessionsSummaryForUser(ownerId));
    ctx.status(200);
  };

  private LocalDate occurrenceFrom(String dateString) {
    return LocalDate.parse(Optional.ofNullable(dateString)
        .orElseThrow(() -> new RuntimeException("No date provided")));
  }

  private Collection<PomodoroSessionDTO> sessionsToDTO(Collection<PomodoroSession> sessions) {
    return sessions.stream().map(session -> PomodoroSessionMapper.toDto.apply(session)).collect(
        Collectors.toList());
  }
}
