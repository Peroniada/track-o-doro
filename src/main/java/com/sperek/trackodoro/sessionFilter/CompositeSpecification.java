package com.sperek.trackodoro.sessionFilter;

public abstract class CompositeSpecification<T> implements Specification<T> {

  @Override
  public Specification<T> and(Specification<T> other) {
    return new AndSpecification<T>(this, other);
  }

  @Override
  public Specification<T> nand(Specification<T> other) {
    return new NandSpecification<T>(this, other);
  }

  @Override
  public Specification<T> or(Specification<T> other) {
    return new OrSpecification<T>(this, other);
  }

  @Override
  public Specification<T> nor(Specification<T> other) {
    return new NorSpecification<T>(this, other);
  }

  @Override
  public Specification<T> not(Specification<T> other) {
    return new NotSpecification<T>(this);
  }
}
