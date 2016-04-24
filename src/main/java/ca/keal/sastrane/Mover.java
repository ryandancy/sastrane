package ca.keal.sastrane;

import lombok.NonNull;

/**
 * An object that returns the result of a player's move, whether that be from an AI, user input, or something else
 * entirely. If you're looking for a general description of a side, go to {@link Player}.
 *
 * @see Player
 */
public interface Mover {
    
    @NonNull
    Move getMove(@NonNull Round round);
    
    @NonNull
    <T extends Enum<T> & Decision> T decide(Class<T> decision);
    
}