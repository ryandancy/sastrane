package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when the user decides a {@link ca.keal.sastrane.api.Decision}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDecideEvent extends TurnEvent {
    
    private Decision decision;
    
    public UserDecideEvent(Round round, Decision decision) {
        super(round);
        this.decision = decision;
    }
    
}