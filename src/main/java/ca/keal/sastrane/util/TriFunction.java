package ca.keal.sastrane.util;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    
    R apply(A a, B b, C c);
    
}