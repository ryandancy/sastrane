package ca.keal.sastrane.event;

import ca.keal.sastrane.Combatant;
import ca.keal.sastrane.Game;
import ca.keal.sastrane.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all events that occur during a turn.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TurnEvent extends GameEvent {
    
    private final Combatant combatant;
    private final Player player;
    
    public TurnEvent(Game game) {
        super(game);
        combatant = game.getCurrentTurn();
        player = game.getCombatantsToPlayers().get(combatant);
    }
    
    /**
     * TurnEvent posted before the turn begins.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends TurnEvent {
        
        public Pre(Game game) {
            super(game);
        }
        
    }
    
    /**
     * TurnEvent posted after the turn ends. Note that the move counter is not incremented yet when this is posted.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends TurnEvent {
        
        public Post(Game game) {
            super(game);
        }
        
    }
    
}