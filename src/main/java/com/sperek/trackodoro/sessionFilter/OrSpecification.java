package com.sperek.trackodoro.sessionFilter;


public class OrSpecification<T> extends CompositeSpecification<T> {

  public OrSpecification(Specification<T> left,
      Specification<T> right) {
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return false;
  }
}
