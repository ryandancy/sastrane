package ca.keal.sastrane.gui;

import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import lombok.SneakyThrows;

public class GameController {
    
    @FXML private Label title;
    @FXML private GridPane boardGrid;
    
    private Round round;
    
    public void setRound(Round round) {
        this.round = round;
        this.title.setText(round.getGame().getName());
        this.round.getBoard().addListener(change -> updateBoardGrid());
        
        for (int x = 0; x <= round.getBoard().getMaxX(); x++) {
            for (int y = 0; y <= round.getBoard().getMaxY(); y++) {
                Node cell;
                NumberBinding cellDimen = Bindings.min(boardGrid.widthProperty().divide(round.getBoard().getMaxX() + 1),
                        boardGrid.heightProperty().divide(round.getBoard().getMaxY() + 1));
                if (round.getBoard().isOn(new Square(x, y))) {
                    ImageView img = new ImageView();
                    img.getStyleClass().addAll("square", (x + (round.getBoard().getMaxY() - y) % 2) % 2 == 0
                            ? "even" : "odd", "x" + x, "y" + y);
                    img.fitHeightProperty().bind(cellDimen);
                    img.fitWidthProperty().bind(cellDimen);
                    img.setPreserveRatio(true);
                    GridPane.setHalignment(img, HPos.CENTER);
                    GridPane.setValignment(img, VPos.CENTER);
                    cell = img;
                } else {
                    Region filler = new Region();
                    filler.setVisible(false);
                    filler.minHeightProperty().bind(cellDimen);
                    filler.minWidthProperty().bind(cellDimen);
                    cell = filler;
                }
                boardGrid.add(cell, round.getBoard().getMaxY() - y, x);
            }
        }
        updateBoardGrid();
    }
    
    @SneakyThrows
    private void updateBoardGrid() {
        for (Square square : round.getBoard()) {
            Node squareNode = GuiUtils.getNodeFromGridPane(boardGrid,
                    square.getYFlipped(round.getBoard()), square.getX());
            if (!(squareNode instanceof ImageView)) continue;
            ImageView squareImage = (ImageView) squareNode;
            
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