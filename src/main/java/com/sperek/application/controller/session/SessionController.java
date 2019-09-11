package com.sperek.application.controller.session;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.core.security.SecurityUtil.roles;
import static io.javalin.plugin.openapi.dsl.OpenApiBuilder.document;
import static io.javalin.plugin.openapi.dsl.OpenApiBuilder.documented;

import com.sperek.application.controller.ApiRole;
import com.sperek.application.controller.query.QueryResolver;
import com.sperek.application.token.Tokenizer;
import com.sperek.trackodoro.PomodoroSessionMapper;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.dto.PomodoroSessionDTO;
import com.sperek.trackodoro.tracker.session.PomodoroSession;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionController {

  private final String CURRENT_USER_ID_HEADER_NAME = "Token";

  private PomodoroTracker tracker;
  private QueryResolver queryResolver;
  private Tokenizer tokenizer;
  private Logger log = LoggerFactory.getLogger(SessionController.class);

  public SessionController(PomodoroTracker tracker, Tokenizer tokenizer) {
    this.tracker = tracker;
    this.tokenizer = tokenizer;
    this.queryResolver = new QueryResolver();
  }

  public EndpointGroup apiRoutes() {
    return sessionRoutes;
  }

  private OpenApiDocumentation saveSessionDoc = document()
      .header(CURRENT_USER_ID_HEADER_NAME, String.class)
      .body(PomodoroSessionDTO.class)
      .result("201")
      .result("403")
      .result("401");

  private Handler saveSession = ctx -> {
    final PomodoroSessionDTO session = ctx.bodyAsClass(PomodoroSessionDTO.class);
    final UUID ownerId = ownerId(ctx);
    tracker.saveSession(PomodoroSessionMapper.fromDto.apply(session, ownerId));
    ctx.status(201);
  };

  private OpenApiDocumentation getSessionsDoc = document()
      .header(CURRENT_USER_ID_HEADER_NAME, String.class)
      .queryParam("date", String.class)
      .queryParam("fromDate", String.class)
      .queryParam("toDate", String.class)
      .queryParam("weekNumber", String.class)
      .queryParam("category", String.class)
      .jsonArray("200", PomodoroSessionDTO.class)
      .result("401")
      .result("403");

  private Handler getSessions = ctx -> {
    final UUID ownerId = ownerId(ctx);
    final Specification<PomodoroSession> specification = queryResolver
        .resolve(ownerId, ctx.queryParamMap());
    final Collection<PomodoroSession> sessions = tracker.findSatisfyingSessions(specification);
    ctx.json(sessionsToDTO(sessions));
    ctx.status(200);
  };

  private OpenApiDocumentation countSessionsDoc = document()
      .header(CURRENT_USER_ID_HEADER_NAME, String.class)
      .queryParam("date", String.class)
      .queryParam("fromDate", String.class)
      .queryParam("toDate", String.class)
      .queryParam("weekNumber", String.class)
      .queryParam("category", String.class)
      .jsonArray("200", PomodoroSessionDTO.class)
      .result("401")
      .result("403");

  private Handler countSessions = ctx -> {
    final UUID ownerId = ownerId(ctx);
    Specification<PomodoroSession> specification = queryResolver
        .resolve(ownerId, ctx.queryParamMap());
    ctx.json(tracker.countSatisfyingSessions(specification));
    ctx.status(200);
  };

  private OpenApiDocumentation getSessionDoc = document()
      .header(CURRENT_USER_ID_HEADER_NAME, String.class)
      .pathParam("id", String.class)
      .json("200", PomodoroSessionDTO.class)
      .result("401")
      .result("403");

  private Handler getSession = ctx -> {
    UUID ownerId = ownerId(ctx);
    final PomodoroSession session = tracker.getSession(UUID.fromString(ctx.pathParam("id")));
    if (ownerId.equals(session.getOwnerId())) {
      ctx.json(PomodoroSessionMapper.toDto.apply(session));
      ctx.status(200);
    } else {
      ctx.status(403);
    }
  };

  private OpenApiDocumentation sessionSummaryDoc = document()
      .header(CURRENT_USER_ID_HEADER_NAME, String.class)
      .jsonArray("200", Map.class)
      .result("401")
      .result("403");

  private Handler sessionsSummaryForUser = ctx -> {
    UUID ownerId = ownerId(ctx);
    ctx.json(tracker.sessionsSummaryForUser(ownerId));
    ctx.status(200);
  };

  private Collection<PomodoroSessionDTO> sessionsToDTO(Collection<PomodoroSession> sessions) {
    return sessions.stream().map(session -> PomodoroSessionMapper.toDto.apply(session)).collect(
        Collectors.toList());
  }

  private EndpointGroup sessionRoutes = () -> path("sessions", () -> {
    get(documented(getSessionsDoc, getSessions), roles(ApiRole.USER));
    post(documented(saveSessionDoc, saveSession), roles(ApiRole.USER));
    path("count", () ->
        get(documented(countSessionsDoc, countSessions), roles(ApiRole.USER)));
    path("summary", () ->
        get(documented(sessionSummaryDoc, sessionsSummaryForUser), roles(ApiRole.USER)));
    path(":id", () ->
        get(documented(getSessionDoc, getSession), roles(ApiRole.USER)));
  });

  @NotNull
  private UUID ownerId(Context ctx) {
    return UUID
        .fromString(
            tokenizer.parser(ctx.header(CURRENT_USER_ID_HEADER_NAME)).getBody().getSubject());
  }
}
