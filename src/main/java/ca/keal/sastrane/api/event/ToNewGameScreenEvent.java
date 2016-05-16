package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Game;
import javafx.scene.Scene;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when something to do with the new game screen transition happens.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ToNewGameScreenEvent extends NewSceneEvent {
    
    private final Game game;
    
    public ToNewGameScreenEvent(Scene previousScene, Scene nextScene, Game game) {
        super(previousScene, nextScene);
        this.game = game;
    }
    
    public Scene getNewGameScene() {
        return getNextScene();
    }
    
    /**
     * Posted at the transition to the new game scene, <i>before</i> {@link
     * ca.keal.sastrane.gui.NewGameController#setGame(Game)} is called.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends ToNewGameScreenEvent {
        
        public Pre(Scene previousScene, Scene nextScene, Game game) {
            super(previousScene, nextScene, game);
        }
        
    }
    
    /**
     * Posted at the transition to the new game screen, <i>after</i> {@link
     * ca.keal.sastrane.gui.NewGameController#setGame(Game)} is called.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends ToNewGameScreenEvent {
    
        public Post(Scene previousScene, Scene nextScene, Game game) {
            super(previousScene, nextScene, game);
        }
        
    }
    
}