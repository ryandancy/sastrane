package ca.keal.sastrane.event;

import ca.keal.sastrane.Game;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Root class for all events that occur in-game.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GameEvent extends RootEvent {
    
    private final Game game;
    
}