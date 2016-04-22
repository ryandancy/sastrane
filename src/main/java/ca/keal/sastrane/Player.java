package ca.keal.sastrane;

import lombok.Data;

/**
 * A general description of a player/team/side/whatever. If you're looking for an AI-or-user input class,
 * go to {@link Mover}.
 * 
 * @see Mover
 */
@Data
public class Player {
    
    private final String name;
    
}