package ca.keal.sastrane;

import lombok.Data;

/**
 * A general description of a player/team/side/whatever. If you're looking for an AI-or-user wrapper class,
 * go to {@link Player}.
 * 
 * @see Player
 */
@Data
public class Combatant {
    
    private final String name;
    
}