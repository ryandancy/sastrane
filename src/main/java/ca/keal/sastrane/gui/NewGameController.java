package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.util.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class NewGameController {
    
    @FXML
    private Label title;
    
    private Game game;
    
    public void setGame(Game game) {
        this.game = game;
        this.title.setText("New " + game.getName() + " Game");
    }
    
    @FXML
    @SneakyThrows
    private void onBack(ActionEvent e) {
        // Send back to the main menu
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml")));
    }
    
}