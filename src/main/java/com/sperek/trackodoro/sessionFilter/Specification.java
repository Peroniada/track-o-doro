package com.sperek.trackodoro.sessionFilter;

public interface Specification<T> {

  boolean isSatisfiedBy(T t);

  Specification<T> and(Specification<T> other);

  Specification<T> nand(Specification<T> other);

  Specification<T> or(Specification<T> other);

  Specification<T> nor(Specification<T> other);

  Specification<T> not(Specification<T> other);


}
