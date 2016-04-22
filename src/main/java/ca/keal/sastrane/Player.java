package ca.keal.sastrane;

import lombok.NonNull;

/**
 * An object that returns the result of a combatant's move, whether that be from an AI, user input, or
 * something else entirely. If you're looking for a general description of a side, go to {@link Combatant}.
 *
 * @see Combatant
 */
public interface Player {
    
    @NonNull
    Move getMove(@NonNull Round round);
    
}