package com.sperek.trackodoro.sessionFilter.composite.spec;

abstract class CompositeSpecification<T> implements Specification<T> {

  Specification<T> left;
  Specification<T> right;

  CompositeSpecification(Specification<T> left, Specification<T> right) {
    this.left = left;
    this.right = right;
  }
}
