package com.sperek.application.controller;

import com.sperek.application.controller.query.QueryResolver;
import com.sperek.trackodoro.PomodoroSessionMapper;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.dto.PomodoroSessionDTO;
import com.sperek.trackodoro.tracker.session.PomodoroSession;
import io.javalin.http.Handler;
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

  public Handler getSessions = ctx -> {
    final UUID ownerId = UUID.fromString(Objects.requireNonNull(ctx.header("Current-User")));
    final Specification<PomodoroSession> specification = queryResolver.resolve(ownerId, ctx.queryParamMap());
    final Collection<PomodoroSession> sessions = tracker.findSatisfyingSessions(specification);
    ctx.json(sessionsToDTO(sessions));
    ctx.status(200);
  };

  public Handler countSessions = ctx -> {
    final UUID ownerId = UUID.fromString(Objects.requireNonNull(ctx.header("Current-User")));
    Specification<PomodoroSession> specification = queryResolver.resolve(ownerId, ctx.queryParamMap());
    ctx.json(tracker.countSessions(specification));
    ctx.status(200);
  };

  public Handler getSession = ctx -> {
    tracker.getSession(UUID.fromString(ctx.pathParam("id")));
    ctx.status(200);
  };

  public Handler sessionsSummaryForUser = ctx -> {
    UUID ownerId = UUID.fromString(Optional.ofNullable(ctx.header("userId"))
        .orElseThrow(() -> new RuntimeException("No userId provided")));
    ctx.json(tracker.sessionsSummaryForUser(ownerId));
    ctx.status(200);
  };

  private Collection<PomodoroSessionDTO> sessionsToDTO(Collection<PomodoroSession> sessions) {
    return sessions.stream().map(session -> PomodoroSessionMapper.toDto.apply(session)).collect(
        Collectors.toList());
  }
}
