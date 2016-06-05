package ca.keal.sastrane.gui;

import ca.keal.sastrane.util.Resource;
import javafx.event.Event;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * A controller with the ability to go back to a previous scene.
 */
@RequiredArgsConstructor
public abstract class GoBacker {
    
    private final Resource previous;
    
    @FXML
    @SneakyThrows
    protected void goBack(Event e) {
        GuiUtils.goTo(previous, e);
    }
    
}