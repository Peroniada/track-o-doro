package com.sperek.application.controller.query;

import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.DateSpecification;
import com.sperek.trackodoro.sessionFilter.OwnerSpecification;
import com.sperek.trackodoro.sessionFilter.WeekOfYearSpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import com.sperek.trackodoro.tracker.session.PomodoroSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class QueryResolver {

  private Map<String, ToSpecificationConverter<String>> queryMapToSpecification;

  public QueryResolver() {
    this.queryMapToSpecification = new HashMap<>();

    final ToSpecificationConverter<String> localDateSpecification = date -> new DateSpecification(
        LocalDate.parse(date));
    queryMapToSpecification.put("date", localDateSpecification);
    queryMapToSpecification.put("fromDate", localDateSpecification);
    queryMapToSpecification.put("toDate", localDateSpecification);
    queryMapToSpecification.put("weekNumber", weekNumber -> new WeekOfYearSpecification(Integer.parseInt(weekNumber)));
    queryMapToSpecification.put("category", (CategorySpecification::new));
  }

  public Specification<PomodoroSession> resolve(UUID currentUser, Map<String, List<String>> queryParamMap) {

    Specification<PomodoroSession> sessionSpecification = new OwnerSpecification(currentUser);

    for (Entry<String, List<String>> elem : queryParamMap.entrySet()) {
      sessionSpecification = sessionSpecification
          .and(queryMapToSpecification.get(elem.getKey()).convert(elem.getValue().get(0)));
    }
    return sessionSpecification;
  }

  @FunctionalInterface
  private interface ToSpecificationConverter<Type> {

    Specification<PomodoroSession> convert(Type type);
  }
}
