package ca.keal.sastrane.gui;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.MovingPiece;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.PlacingMove;
import ca.keal.sastrane.PlacingPiece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.event.TurnEvent;
import ca.keal.sastrane.event.UserMoveEvent;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.css.PseudoClass;
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
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController {
    
    @FXML private BorderPane game;
    @FXML private Label title;
    @FXML private GridPane boardGrid;
    @FXML private TilePane pieceChooser;
    
    private Round round;
    private Map<Player, ToggleGroup> playersToPieceChooserGroups = null;
    @Setter private boolean inputting = false;
    
    private Square selectionBase = null;
    private List<Square> selection = new ArrayList<>();
    
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
                    imgPane.getStyleClass().addAll("square", (x + (round.getBoard().getMaxY() - y) % 2) % 2 == 0
                            ? "even" : "odd", "x" + x, "y" + y);
                    
                    ImageView img = new ImageView();
                    img.getStyleClass().add("img");
                    img.fitHeightProperty().bind(cellDimen);
                    img.fitWidthProperty().bind(cellDimen);
                    img.setPreserveRatio(true);
                    GridPane.setHalignment(img, HPos.CENTER);
                    GridPane.setValignment(img, VPos.CENTER);
                    
                    // Overlay for styling with CSS
                    Region overlay = new Region();
                    overlay.getStyleClass().add("overlay");
                    overlay.minHeightProperty().bind(cellDimen);
                    overlay.minWidthProperty().bind(cellDimen);
                    
                    imgPane.getChildren().addAll(img, overlay);
                    
                    final int finalX = x, finalY = y;
                    imgPane.setOnMouseClicked(e -> onTileClick(finalX, finalY));
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
        
        round.start();
    }
    
    @SneakyThrows
    private void updateBoardGrid() {
        for (Square square : round.getBoard()) {
            Node squareNode = GuiUtils.getNodeFromGridPane(boardGrid,
                    square.getYFlipped(round.getBoard()), square.getX());
            if (!(squareNode instanceof StackPane)) continue;
            StackPane squarePane = (StackPane) squareNode;
            if (!(squarePane.getChildren().size() >= 1 && squarePane.getChildren().get(0) instanceof ImageView)) {
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
            pieceButton.setUserData(piece);
        }
        pieceButton.getStyleClass().add("placing-piece-button");
        group.getToggles().add(pieceButton);
    }
    
    private void onTileClick(int x, int y) {
        if (!inputting) return;
    
        Square square = new Square(x, y);
        if (selection.size() > 0) {
            if (selection.contains(square)) {
                round.getGame().getBus().post(new UserMoveEvent(round, selectionBase.to(square)));
            } else {
                deselect();
            }
            return;
        }
        
        PlacingPiece placingPiece = getCurrentPlacingPiece();
        if (placingPiece == null) {
            // selecting
            Pair<Piece, Player> atCoords = round.getBoard().get(square);
            if (atCoords == null || !(atCoords.getLeft() instanceof MovingPiece)) return;
            List<Move> possibleMoves = ((MovingPiece) atCoords.getLeft()).getPossibleMoves(round, square,
                    atCoords.getRight());
            if (possibleMoves.size() == 0) return;
            select(square, possibleMoves.stream()
                    .map(Move::getEndPos)
                    .collect(Collectors.toList()));
        } else {
            // placing
            List<Square> placements = placingPiece.getPossiblePlacements(round, round.getCurrentTurn()).stream()
                    .map(PlacingMove::getPos)
                    .collect(Collectors.toList());
            if (placements.contains(square)) {
                round.getGame().getBus().post(new UserMoveEvent(round, new PlacingMove(Pair.of(placingPiece,
                        round.getCurrentTurn()), square)));
            }
        }
    }
    
    private void select(Square selectionBase, List<Square> selection) {
        this.selectionBase = selectionBase;
        this.selection = selection;
        
        lookup(selectionBase).getPseudoClassStates().add(PseudoClass.getPseudoClass("selected-base"));
        for (Square square : selection) {
            lookup(square).getPseudoClassStates().add(PseudoClass.getPseudoClass("selected"));
        }
    }
    
    private void deselect() {
        lookup(selectionBase).getPseudoClassStates().remove(PseudoClass.getPseudoClass("selected-base"));
        for (Square square : selection) {
            lookup(square).getPseudoClassStates().remove(PseudoClass.getPseudoClass("selected"));
        }
        
        selectionBase = null;
        selection.clear();
    }
    
    private Node lookup(Square square) {
        return game.getScene().lookup(getLookupString(square));
    }
    
    private String getLookupString(Square square) {
        return String.format(".x%d.y%d", square.getX(), square.getY());
    }
    
    private PlacingPiece getCurrentPlacingPiece() {
        if (round.getGame().getPlacingPieces().length == 0) return null;
        if (playersToPieceChooserGroups == null) return round.getGame().getPlacingPieces()[0];
        return (PlacingPiece) playersToPieceChooserGroups.get(round.getCurrentTurn()).getUserData();
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