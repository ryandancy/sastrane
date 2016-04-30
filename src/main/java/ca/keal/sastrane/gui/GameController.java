package ca.keal.sastrane.gui;

import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

public class GameController {
    
    @FXML private Label title;
    @FXML private GridPane boardGrid;
    
    private Round round;
    
    public void setRound(Round round) {
        this.round = round;
        this.title.setText(round.getGame().getName());
        this.round.getBoard().addListener(change -> updateBoardGrid());
        
        for (Square square : round.getBoard()) {
            boardGrid.add(new ImageView(), square.getX(), round.getBoard().getMaxY() - square.getY());
        }
        updateBoardGrid();
    }
    
    @SneakyThrows
    private void updateBoardGrid() {
        for (Square square : round.getBoard()) {
            ImageView squareImage = (ImageView) GuiUtils.getNodeFromGridPane(boardGrid, square.getX(),
                    round.getBoard().getMaxY() - square.getY());
            if (squareImage == null) continue; // shouldn't happen
            
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            squareImage.setImage((atSquare == null) ? null : new Image(atSquare.getLeft().getImage()
                    .mangle(atSquare.getRight().getName()).get().openStream()));
        }
    }
    
    @FXML
    @SneakyThrows
    private void onQuit(ActionEvent e) {
        // Send back to the main menu
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml")));
    }
    
}