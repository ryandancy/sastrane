package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.move.Move;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when the user chooses a move.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMoveEvent extends MoveEvent {
    
    public UserMoveEvent(Round round, Move move) {
        super(round, move);
    }
    
}