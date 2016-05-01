package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Posted when the user chooses a move.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMoveEvent extends MoveEvent {
    
    public UserMoveEvent(@NonNull Round round, @NonNull Move move) {
        super(round, move);
    }
    
}