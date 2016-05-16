package ca.keal.sastrane.api.event;

import javafx.scene.Scene;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when anything to do with the GUI happens.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GuiEvent extends RootEvent {
    
    private final Scene scene;
    
}