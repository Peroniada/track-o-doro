package com.sperek.application.controller.query;

import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.DateSpecification;
import com.sperek.trackodoro.sessionFilter.WeekOfYearSpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import com.sperek.trackodoro.tracker.session.PomodoroSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResolver {

  private Map<String, ToSpecificationConverter<String>> queryMapToSpecification = queryMapToSpecification();

  public Specification<PomodoroSession> resolve(Map<String, List<String>> queryParamMap) {
    Specification<PomodoroSession> sessionSpecification = pomodoroSession -> true;
    queryParamMap.forEach((key, value) -> {
      sessionSpecification.and(queryMapToSpecification.get(key).convert(value.get(0)));
    });
    return sessionSpecification;
  }

  private Map<String, ToSpecificationConverter<String>> queryMapToSpecification() {
    final HashMap<String, ToSpecificationConverter<String>> map = new HashMap<>();

    final ToSpecificationConverter<String> localDateSpecification = date -> new DateSpecification(
        LocalDate.parse(date));
    map.put("category", (CategorySpecification::new));
    map.put("date", localDateSpecification);
    map.put("fromDate", localDateSpecification);
    map.put("toDate", localDateSpecification);
    map.put("weekNumber", weekNumber -> new WeekOfYearSpecification(Integer.parseInt(weekNumber)));
    return map;
  }

  @FunctionalInterface
  private interface ToSpecificationConverter<Type> {
    Specification<PomodoroSession> convert(Type type);
  }
}
