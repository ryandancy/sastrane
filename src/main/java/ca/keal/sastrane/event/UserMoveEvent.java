package ca.keal.sastrane.event;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Posted when the user decides on a move.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMoveEvent extends MoveEvent {
    
    public UserMoveEvent(@NonNull Round round, @NonNull Move move) {
        super(round, move);
    }
    
}