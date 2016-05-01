package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Root class for all events that occur in-game.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoundEvent extends RootEvent {
    
    private final Round round;
    
    /**
     * RoundEvent posted just before the first turn of a round.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends RoundEvent {
        
        public Pre(Round round) {
            super(round);
        }
        
    }
    
    /**
     * RoundEvent posted just after a round ends.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends RoundEvent {
        
        public Post(Round round) {
            super(round);
        }
        
    }
    
}