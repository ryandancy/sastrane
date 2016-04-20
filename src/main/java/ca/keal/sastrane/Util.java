package ca.keal.sastrane;

import com.google.common.collect.HashMultiset;

import java.util.Collection;

public final class Util {
    
    private Util() {
        throw new RuntimeException("ca.keal.sastrane.Util is a utility class and thus cannot be instantiated.");
    }
    
    /**
     * Returns whether the two collections have equal elements; that is, the same number of elements, and
     * all elements have an equal counterpart on the other set. Disregards order.
     */
    public static <E> boolean areElementsEqual(Collection<E> a, Collection<E> b) {
        return HashMultiset.create(a).equals(HashMultiset.create(b));
    }
    
}