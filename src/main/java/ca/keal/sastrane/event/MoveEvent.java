package ca.keal.sastrane.event;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.Move;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all events that occur during the movement of a piece.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MoveEvent extends TurnEvent {
    
    private final Move move;
    
    public MoveEvent(Game game, Move move) {
        super(game);
        this.move = move;
    }
    
    /**
     * MoveEvent posted just before a move.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends MoveEvent {
        
        public Pre(Game game, Move move) {
            super(game, move);
        }
        
    }
    
    /**
     * MoveEvent posted just after a move.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends MoveEvent {
        
        public Post(Game game, Move move) {
            super(game, move);
        }
        
    }
    
}