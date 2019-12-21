package com.sperek.pomodorotracker.domain.sessionFilter.composite.spec;

public class NorSpecification<T> extends CompositeSpecification<T> {

  NorSpecification(Specification<T> left, Specification<T> right) {
    super(left, right);
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    OrSpecification<T> orSpecification = new OrSpecification<>(left, right);
    return new NotSpecification<>(orSpecification).isSatisfiedBy(t);
  }
}
