package ca.keal.sastrane.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;

/**
 * Mutable 2-tuple class.
 */
@Data
@AllArgsConstructor
public class Pair<L, R> {
    
    @Nullable private L left;
    @Nullable private R right;
    
    public static <L, R> Pair<L, R> of(@Nullable L left, @Nullable R right) {
        return new Pair<>(left, right);
    }
    
    public <T> Pair<T, R> withLeft(@Nullable T newLeft) {
        return new Pair<>(newLeft, right);
    }
    
    public <T> Pair<L, T> withRight(@Nullable T newRight) {
        return new Pair<>(left, newRight);
    }
    
}