package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.TurnEvent;
import ca.keal.sastrane.api.event.UserDecideEvent;
import ca.keal.sastrane.api.event.UserMoveEvent;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.concurrent.Task;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import lombok.NonNull;
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
    @FXML private FlowPane decisionPane;
    
    private Round round;
    private Map<Player, ToggleGroup> playersToPieceChooserGroups = null;
    @Setter private boolean inputting = false;
    private boolean deciding = false;
    
    private Square selectionBase = null;
    private List<Square> selection = new ArrayList<>();
    
    public void setRound(@NonNull Round round) {
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
        
        for (int y = 0; y <= round.getBoard().getMaxX(); y++) {
            for (int x = 0; x <= round.getBoard().getMaxY(); x++) {
                Node cell;
                NumberBinding cellDimen = Bindings.min(boardGrid.widthProperty().divide(round.getBoard().getMaxX() + 1),
                        boardGrid.heightProperty().divide(round.getBoard().getMaxY() + 1));
                if (round.getBoard().isOn(new Square(x, y))) {
                    StackPane imgPane = new StackPane();
                    imgPane.getStyleClass().addAll("square", (x + y % 2) % 2 == 0 ? "even" : "odd", "x" + x, "y" + y);
                    
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
                boardGrid.add(cell, x, y);
            }
        }
        updateBoardGrid();
        
        Thread worker = new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                round.start();
                return null;
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
    
    @SneakyThrows
    private void updateBoardGrid() {
        for (Square square : round.getBoard()) {
            Node squareNode = GuiUtils.getNodeFromGridPane(boardGrid, square.getX(), square.getY());
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
        if (!inputting || deciding) return;
    
        Square square = new Square(x, y);
        if (selection.size() > 0) {
            if (selection.contains(square)) {
                round.getGame().getBus().post(new UserMoveEvent(round, selectionBase.to(square)));
            }
            deselect();
            return;
        }
        
        PlacingPiece placingPiece = getCurrentPlacingPiece();
        if (placingPiece == null) {
            // selecting
            Pair<Piece, Player> atCoords = round.getBoard().get(square);
            if (atCoords == null || !(atCoords.getLeft() instanceof MovingPiece)
                    || atCoords.getRight() != round.getCurrentTurn()) return;
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
        
        lookup(selectionBase).pseudoClassStateChanged(PseudoClass.getPseudoClass("selection-base"), true);
        for (Square square : selection) {
            lookup(square).pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        }
    }
    
    private void deselect() {
        lookup(selectionBase).pseudoClassStateChanged(PseudoClass.getPseudoClass("selection-base"), false);
        for (Square square : selection) {
            lookup(square).pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
        }
        
        selectionBase = null;
        selection.clear();
    }
    
    @NonNull
    @SneakyThrows
    public void displayDecision(@NonNull Decision[] options) {
        deciding = true;
        
        for (Decision option : options) {
            VBox optionBox = new VBox();
            optionBox.getStyleClass().add("option");
            optionBox.setOnMouseClicked(e -> onDecide(option));
            optionBox.prefWidthProperty().bind(boardGrid.widthProperty().multiply(.5));
            optionBox.prefHeightProperty().bind(optionBox.prefWidthProperty());
            optionBox.setMaxWidth(Region.USE_PREF_SIZE);
            optionBox.setMaxHeight(Region.USE_PREF_SIZE);
            optionBox.setMinWidth(Region.USE_PREF_SIZE);
            optionBox.setMinHeight(Region.USE_PREF_SIZE);
            
            ImageView img = new ImageView(new Image(option.getIcon().get().openStream()));
            img.getStyleClass().add("img");
            img.fitHeightProperty().bind(optionBox.heightProperty().multiply(.6));
            img.setPreserveRatio(true);
            
            Label label = new Label(option.getName());
            label.getStyleClass().add("text");
            label.setLabelFor(img);
            
            optionBox.getChildren().addAll(img, label);
            decisionPane.getChildren().add(optionBox);
            decisionPane.setVisible(true);
            decisionPane.setMouseTransparent(false);
        }
    }
    
    private void onDecide(Decision option) {
        deciding = false;
        decisionPane.getChildren().clear();
        decisionPane.setVisible(false);
        decisionPane.setMouseTransparent(true);
        
        round.getGame().getBus().post(new UserDecideEvent(round, option));
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