package com.sperek.trackodoro.sessionFilter;

public class AndSpecification<T> extends CompositeSpecification<T> {

  public AndSpecification(Specification<T> left, Specification right) {

  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return false;
  }
}
