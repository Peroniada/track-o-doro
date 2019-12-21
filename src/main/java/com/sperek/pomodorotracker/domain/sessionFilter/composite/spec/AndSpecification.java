package com.sperek.pomodorotracker.domain.sessionFilter.composite.spec;

public class AndSpecification<T> extends CompositeSpecification<T> {

  AndSpecification(Specification<T> left, Specification<T> right) {
    super(left, right);
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return this.left.isSatisfiedBy(t) && this.right.isSatisfiedBy(t);
  }
}
