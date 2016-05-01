package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all events that occur during a turn.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TurnEvent extends RoundEvent {
    
    private final Player player;
    private final Mover mover;
    
    public TurnEvent(Round round) {
        super(round);
        player = round.getCurrentTurn();
        mover = round.getPlayersToMovers().get(player);
    }
    
    /**
     * TurnEvent posted before the turn begins.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends TurnEvent {
        
        public Pre(Round round) {
            super(round);
        }
        
    }
    
    /**
     * TurnEvent posted after the turn ends. Note that the move counter is not incremented yet when this is posted.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends TurnEvent {
        
        public Post(Round round) {
            super(round);
        }
        
    }
    
}