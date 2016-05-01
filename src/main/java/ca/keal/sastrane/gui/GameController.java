package ca.keal.sastrane.gui;

import ca.keal.sastrane.Piece;
import ca.keal.sastrane.PlacingPiece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.event.TurnEvent;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController {
    
    @FXML private BorderPane game;
    @FXML private Label title;
    @FXML private GridPane boardGrid;
    @FXML private TilePane pieceChooser;
    
    private Round round;
    private Map<Player, ToggleGroup> playersToPieceChooserGroups = null;
    
    public void setRound(Round round) {
        this.round = round;
        this.round.getBoard().addListener(change -> updateBoardGrid());
        title.setText(round.getGame().getName());
        game.getStylesheets().add(round.getGame().getCss().getFilename());
        
        int numPlacingPieces = round.getGame().getPlacingPieces().length;
        if (numPlacingPieces > 1 || (numPlacingPieces == 1 && !round.getGame().isPlaceOnly())) {
            playersToPieceChooserGroups = new HashMap<>();
            for (Player player : round.getGame().getPlayers()) {
                ToggleGroup pieceChooserGroup = new ToggleGroup();
                for (PlacingPiece piece : round.getGame().getPlacingPieces()) {
                    addPlacingPiece(piece, player, pieceChooserGroup);
                }
                playersToPieceChooserGroups.put(player, pieceChooserGroup);
            }
            round.getGame().getBus().register(this);
            pieceChooser.setVisible(true);
        }
        
        for (int x = 0; x <= round.getBoard().getMaxX(); x++) {
            for (int y = 0; y <= round.getBoard().getMaxY(); y++) {
                Node cell;
                NumberBinding cellDimen = Bindings.min(boardGrid.widthProperty().divide(round.getBoard().getMaxX() + 1),
                        boardGrid.heightProperty().divide(round.getBoard().getMaxY() + 1));
                if (round.getBoard().isOn(new Square(x, y))) {
                    StackPane imgPane = new StackPane();
                    ImageView img = new ImageView();
                    imgPane.getStyleClass().addAll("square", (x + (round.getBoard().getMaxY() - y) % 2) % 2 == 0
                            ? "even" : "odd", "x" + x, "y" + y);
                    img.getStyleClass().add("img");
                    img.fitHeightProperty().bind(cellDimen);
                    img.fitWidthProperty().bind(cellDimen);
                    img.setPreserveRatio(true);
                    GridPane.setHalignment(img, HPos.CENTER);
                    GridPane.setValignment(img, VPos.CENTER);
                    imgPane.getChildren().add(img);
                    cell = imgPane;
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
            if (!(squareNode instanceof StackPane)) continue;
            StackPane squarePane = (StackPane) squareNode;
            if (!(squarePane.getChildren().size() == 1 && squarePane.getChildren().get(0) instanceof ImageView)) {
                continue;
            }
            ImageView squareImage = (ImageView) squarePane.getChildren().get(0);
            
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            squareImage.setImage((atSquare == null) ? null : new Image(atSquare.getLeft().getImage()
                    .mangle(atSquare.getRight().getName()).get().openStream()));
        }
    }
    
    /** {@code piece == null} => "hand" with null player */
    @SneakyThrows
    private void addPlacingPiece(PlacingPiece piece, Player player, ToggleGroup group) {
        ToggleButton pieceButton = new ToggleButton();
        if (piece == null) {
            pieceButton.setGraphic(new ImageView(new Image(new Resource("ca.keal.sastrane.icon", "cursor.png").get()
                    .openStream())));
            pieceButton.setUserData(null);
        } else {
            pieceButton.setGraphic(new ImageView(new Image(piece.getImage().mangle(player.getName()).get()
                    .openStream())));
            pieceButton.setUserData(piece.getImage().getUnmangled());
        }
        pieceButton.getStyleClass().add("placing-piece-button");
        group.getToggles().add(pieceButton);
    }
    
    @Subscribe
    public void beforeTurn(TurnEvent.Pre e) {
        Player current = e.getRound().getCurrentTurn();
        if (playersToPieceChooserGroups != null
                && e.getRound().getPlayersToMovers().get(current) instanceof HumanMover) {
            pieceChooser.getChildren().clear();
            pieceChooser.getChildren().addAll(playersToPieceChooserGroups.get(current).getToggles().stream()
                .map(toggle -> (Node) toggle)
                .collect(Collectors.toList()));
        }
    }
    
    @FXML
    @SneakyThrows
    private void onQuit(ActionEvent e) {
        // Send back to the main menu
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml")));
    }
    
}