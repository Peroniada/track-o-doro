package com.sperek.trackodoro.sessionFilter;

public class NorSpecification<T> extends CompositeSpecification<T> {

  public NorSpecification(Specification<T> left,
      Specification<T> right) {
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return false;
  }
}
