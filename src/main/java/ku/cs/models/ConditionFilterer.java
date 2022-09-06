package ku.cs.models;

public interface ConditionFilterer<T> {
    boolean match(T t);
}
