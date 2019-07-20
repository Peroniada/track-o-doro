package com.sperek.trackodoro.sessionFilter.composite.spec;

public class NandSpecification<T> extends CompositeSpecification<T> {

  NandSpecification(Specification<T> left,
                    Specification<T> right) {
    super(left,right);
  }

  @Override
  public boolean isSatisfiedBy(T t) {
    AndSpecification<T> andSpecification = new AndSpecification<>(left, right);
    return new NotSpecification<>(andSpecification).isSatisfiedBy(t);
  }
}
