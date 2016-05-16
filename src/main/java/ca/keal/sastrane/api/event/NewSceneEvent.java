package ca.keal.sastrane.api.event;

import javafx.scene.Scene;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when a game-specific scene transition occurs.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewSceneEvent extends GuiEvent {
    
    private final Scene previousScene;
    
    public NewSceneEvent(Scene previousScene, Scene nextScene) {
        super(nextScene);
        this.previousScene = previousScene;
    }
    
    public Scene getNextScene() {
        return getScene();
    }
    
}