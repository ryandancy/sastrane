package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import javafx.scene.Scene;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Posted when something to do with the transition to the game screen happens.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ToGameEvent extends NewSceneEvent {
    
    private final Round round;
    private final Map<Player, Mover> playersToMovers;
    
    public ToGameEvent(Scene previousScene, Scene nextScene, Round round, Map<Player, Mover> playersToMovers) {
        super(previousScene, nextScene);
        this.round = round;
        this.playersToMovers = playersToMovers;
    }
    
    public Scene getGameScene() {
        return getNextScene();
    }
    
    /**
     * Posted at the transition to the new game screen, <i>before</i> {@link
     * ca.keal.sastrane.gui.GameController#setRound(Round)} is called.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends ToGameEvent {
        
        public Pre(Scene previousScene, Scene nextScene, Round round, Map<Player, Mover> playersToMovers) {
            super(previousScene, nextScene, round, playersToMovers);
        }
        
    }
    
    /**
     * Posted at the transition to the new game screen, <i>after</i> {@link
     * ca.keal.sastrane.gui.GameController#setRound(Round)} is called.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends ToGameEvent {
        
        public Post(Scene previousScene, Scene nextScene, Round round, Map<Player, Mover> playersToMovers) {
            super(previousScene, nextScene, round, playersToMovers);
        }
        
    }
    
}