package ca.keal.sastrane.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Mutable 2-tuple class.
 */
@Data
@AllArgsConstructor
public class Pair<L, R> {
    
    private L left;
    private R right;
    
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
    
}