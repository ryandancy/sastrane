package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.util.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
@Setter
public class NewGameController implements Initializable {
    
    private Game game;
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    @FXML
    @SneakyThrows
    private void onBack(ActionEvent e) {
        // Send back to the main menu
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml")));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}