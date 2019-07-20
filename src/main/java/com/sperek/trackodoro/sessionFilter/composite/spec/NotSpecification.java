package com.sperek.trackodoro.sessionFilter.composite.spec;

public class NotSpecification<T> implements Specification<T> {

  private final Specification<T> other;

  NotSpecification(Specification<T> other) {
    this.other = other;
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return !this.other.isSatisfiedBy(t);
  }
}
