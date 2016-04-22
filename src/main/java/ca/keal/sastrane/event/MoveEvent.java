package ca.keal.sastrane.event;

import ca.keal.sastrane.Round;
import ca.keal.sastrane.Move;
import ca.keal.sastrane.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all events that occur during the movement of a piece.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MoveEvent extends TurnEvent {
    
    private final Move move;
    
    public MoveEvent(Round round, Move move) {
        super(round);
        this.move = move;
    }
    
    /**
     * MoveEvent posted just before a move.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends MoveEvent {
        
        public Pre(Round round, Move move) {
            super(round, move);
        }
        
    }
    
    /**
     * MoveEvent posted just after a move.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends MoveEvent {
        
        public Post(Round round, Move move) {
            super(round, move);
        }
        
    }
    
}