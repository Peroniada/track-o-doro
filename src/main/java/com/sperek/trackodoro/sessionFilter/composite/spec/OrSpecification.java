package com.sperek.trackodoro.sessionFilter.composite.spec;


public class OrSpecification<T> extends CompositeSpecification<T> {

  OrSpecification(Specification<T> left, Specification<T> right) {
    super(left, right);
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    return left.isSatisfiedBy(t) || right.isSatisfiedBy(t);
  }
}
