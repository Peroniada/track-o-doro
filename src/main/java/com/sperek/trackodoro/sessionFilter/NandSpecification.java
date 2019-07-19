package com.sperek.trackodoro.sessionFilter;

public class NandSpecification<T> extends CompositeSpecification<T> {

  public NandSpecification(Specification<T> left,
      Specification<T> right) {
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return false;
  }
}
