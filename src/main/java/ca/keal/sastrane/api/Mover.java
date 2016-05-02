package ca.keal.sastrane.api;

import ca.keal.sastrane.api.move.Move;

/**
 * An object that returns the result of a player's move, whether that be from an AI, user input, or something else
 * entirely. If you're looking for a general description of a side, go to {@link Player}.
 *
 * @see Player
 */
public interface Mover {
    
    Move getMove(Round round, Player player);
    
    Decision decide(Decision[] options, Round round, Player player);
    
}