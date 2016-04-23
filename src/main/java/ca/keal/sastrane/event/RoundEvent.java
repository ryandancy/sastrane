package ca.keal.sastrane.event;

import ca.keal.sastrane.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Root class for all events that occur in-game.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoundEvent extends RootEvent {
    
    private final Round round;
    
}