package com.sperek.pomodorotracker.domain.sessionFilter.composite.spec;

public interface Specification<T> {

  boolean isSatisfiedBy(T t);

  default Specification<T> and(Specification<T> other) {
    return new AndSpecification<>(this, other);
  }

  default Specification<T> nand(Specification<T> other) {
    return new NandSpecification<>(this, other);
  }

  default Specification<T> or(Specification<T> other) {
    return new OrSpecification<>(this, other);
  }

  default Specification<T> nor(Specification<T> other) {
    return new NorSpecification<>(this, other);
  }

  default Specification<T> not(Specification<T> other) {
    return new NotSpecification<>(other);
  }

}
