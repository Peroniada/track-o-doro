package com.sperek.trackodoro.sessionFilter;

public class NotSpecification<T> extends CompositeSpecification<T> {

  public NotSpecification(Specification<T> other) {
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return false;
  }
}
