/*
 * Copyright (C) 2016 Ryan Dancy
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Notatable;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.TurnEvent;
import ca.keal.sastrane.api.event.UserDecideEvent;
import ca.keal.sastrane.api.event.UserMoveEvent;
import ca.keal.sastrane.api.event.WinEvent;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.text.Font;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ca.keal.sastrane.gui.GuiUtils.lookup;

public class GameController extends GoBacker implements Initializable {
    
    @FXML private BorderPane game;
    @FXML private Label title;
    @FXML private GridPane boardGrid;
    @FXML private TilePane pieceChooser;
    
    @FXML private FlowPane decisionPane;
    
    @FXML private VBox winPane;
    @FXML private ImageView winImg;
    @FXML private Label winText;
    @FXML private Button winBtn;
    @FXML private Button notationBtn;
    
    private Round round;
    @Nullable private Map<Player, ToggleGroup> playersToPieceChooserGroups = null;
    @Setter private boolean inputting = false;
    private boolean deciding = false;
    
    @Nullable private Square selectionBase = null;
    private List<Square> selection = new ArrayList<>();
    private List<Move> selectionMoves = new ArrayList<>();
    
    public GameController() {
        super(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"));
    }
    
    @SneakyThrows
    public void setRound(Round round) {
        this.round = round;
        this.round.getBoard().addListener(change -> updateBoardGrid());
        this.round.getGame().getBus().register(this);
        
        String titleText = I18n.localize(round.getGame().getInfo().getI18nName());
        GuiUtils.setTitle(titleText);
        title.setText(titleText);
        
        game.getStylesheets().add(round.getGame().getInfo().getCss().getFilename());
        
        int numPlacingPieces = round.getGame().getInfo().getPlacingPieces().length;
        if (numPlacingPieces > 1 || (numPlacingPieces == 1 && !round.getGame().getInfo().isPlaceOnly())) {
            playersToPieceChooserGroups = new HashMap<>();
            for (Player player : round.getGame().getInfo().getPlayers()) {
                ToggleGroup pieceChooserGroup = new ToggleGroup();
                for (PlacingPiece piece : round.getGame().getInfo().getPlacingPieces()) {
                    addPlacingPiece(piece, player, pieceChooserGroup);
                }
                playersToPieceChooserGroups.put(player, pieceChooserGroup);
            }
            pieceChooser.setVisible(true);
        }
        
        for (int y = 0; y <= round.getBoard().getMaxY(); y++) {
            for (int x = 0; x <= round.getBoard().getMaxX(); x++) {
                Node cell;
                NumberBinding cellDimen = Bindings.min(boardGrid.widthProperty().divide(round.getBoard().getMaxX() + 1),
                        boardGrid.heightProperty().divide(round.getBoard().getMaxY() + 1));
                if (round.getBoard().isOn(new Square(x, y))) {
                    StackPane imgPane = new StackPane();
                    imgPane.getStyleClass().addAll("square", (x + y % 2) % 2 == 0 ? "even" : "odd", "x" + x, "y" + y);
                    if (x == round.getBoard().getMaxX()) {
                        imgPane.getStyleClass().add("maxx");
                    }
                    if (y == round.getBoard().getMaxY()) {
                        imgPane.getStyleClass().add("maxy");
                    }
                    
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
        
        // Total hack: check CSS file for grid-type property (it's a looked-up colour so the CSS parser accepts it)
        String css = Files.toString(new File(round.getGame().getInfo().getCss().get().toURI()), StandardCharsets.UTF_8);
        if (css.matches("(?s).*(?:-sastrane-)?grid-type:\\s*point.*")) {
            usePoints();
        }
        
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
    
    private void usePoints() {
        DoubleBinding gridTranslate = ((StackPane) boardGrid.getChildren().get(0)).widthProperty().divide(2);
        DoubleBinding translate = gridTranslate.negate();
        
        for (Node child : boardGrid.getChildren()) {
            Node img = child.lookup(".img");
            if (img == null) continue;
            img.translateXProperty().bind(translate);
            img.translateYProperty().bind(translate);
            
            Node overlay = child.lookup(".overlay");
            overlay.translateXProperty().bind(translate);
            overlay.translateYProperty().bind(translate);
        }
        
        boardGrid.translateXProperty().bind(gridTranslate);
        boardGrid.translateYProperty().bind(gridTranslate);
        
        boardGrid.lookupAll(".square.maxx").forEach(s -> s.setStyle("-fx-border-color: transparent transparent "
                + "transparent black; -fx-background-color: transparent;"));
        boardGrid.lookupAll(".square.maxy").forEach(s -> s.setStyle("-fx-border-color: black transparent transparent "
                + "transparent; -fx-background-color: transparent;"));
        boardGrid.lookup(".square.maxx.maxy").setStyle("-fx-border-color: transparent;"
                + "-fx-background-color: transparent;");
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
            
            OwnedPiece atSquare = round.getBoard().get(square);
            squareImage.setImage((atSquare == null) ? null : new Image(atSquare.getPiece().getImage()
                    .mangle(atSquare.getOwner().getName()).get().openStream()));
        }
    }
    
    /** {@code piece == null} => "hand" with null player */
    @SneakyThrows
    private void addPlacingPiece(@Nullable PlacingPiece piece, Player player, ToggleGroup group) {
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
        if (selection.size() > 0 && selectionBase != null) {
            if (selection.contains(square)) {
                SoundEffects.play("click");
                round.getGame().getBus().post(new UserMoveEvent(round, selectionMoves.stream()
                        .filter(move -> move.getEndPos().equals(square))
                        .collect(Collectors.toList())
                        .get(0)));
            }
            deselect();
            return;
        }
        
        PlacingPiece placingPiece = getCurrentPlacingPiece();
        if (placingPiece == null) {
            // selecting
            OwnedPiece atCoords = round.getBoard().get(square);
            if (atCoords == null || !(atCoords.getPiece() instanceof MovingPiece)
                    || atCoords.getOwner() != round.getCurrentTurn()) return;
            List<Move> possibleMoves = ((MovingPiece) atCoords.getPiece()).getPossibleMoves(round, square,
                    atCoords.getOwner());
            if (possibleMoves.size() == 0) return;
            select(square, possibleMoves, possibleMoves.stream()
                    .map(Move::getEndPos)
                    .collect(Collectors.toList()));
        } else {
            // placing
            List<PlacingMove> placements = placingPiece.getPossiblePlacements(round, round.getCurrentTurn());
            if (placements.stream().map(PlacingMove::getPos).anyMatch(square::equals)) {
                SoundEffects.play("click");
                round.getGame().getBus().post(new UserMoveEvent(round, placements.stream()
                        .filter(placement -> placement.getPos().equals(square))
                        .findAny().get()));
            }
        }
    }
    
    private void select(Square selectionBase, List<Move> possibleMoves, List<Square> selection) {
        SoundEffects.play("click");
        
        this.selectionBase = selectionBase;
        this.selection = selection;
        selectionMoves = possibleMoves;
        
        lookup(game, selectionBase).pseudoClassStateChanged(PseudoClass.getPseudoClass("selection-base"), true);
        for (Square square : selection) {
            lookup(game, square).pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        }
    }
    
    private void deselect() {
        assert selectionBase != null;
        lookup(game, selectionBase).pseudoClassStateChanged(PseudoClass.getPseudoClass("selection-base"), false);
        for (Square square : selection) {
            lookup(game, square).pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
        }
        
        selectionBase = null;
        selection.clear();
        selectionMoves.clear();
    }
    
    @SneakyThrows
    public void displayDecision(Decision[] options) {
        deciding = true;
        
        for (Decision option : options) {
            VBox optionBox = new VBox();
            optionBox.getStyleClass().add("option");
            optionBox.setOnMouseClicked(e -> onDecide(option));
            NumberBinding minWH = Bindings.min(boardGrid.widthProperty(), boardGrid.heightProperty()).multiply(0.5);
            optionBox.prefWidthProperty().bind(minWH);
            optionBox.prefHeightProperty().bind(minWH);
            optionBox.setMaxWidth(Region.USE_PREF_SIZE);
            optionBox.setMaxHeight(Region.USE_PREF_SIZE);
            optionBox.setMinWidth(0);
            optionBox.setMinHeight(0);
            
            ImageView img = new ImageView(new Image(option.getIcon().get().openStream()));
            img.getStyleClass().add("img");
            img.fitHeightProperty().bind(optionBox.heightProperty().multiply(.6));
            img.setPreserveRatio(true);
            
            Label label = new Label(I18n.localize(option.getI18nName()));
            label.getStyleClass().add("name");
            label.setLabelFor(img);
            label.fontProperty().bind(Bindings.createObjectBinding(
                    () -> new Font(optionBox.heightProperty().divide(10).get()),
                    optionBox.heightProperty()
            ));
            
            optionBox.getChildren().addAll(img, label);
            decisionPane.getChildren().add(optionBox);
            decisionPane.setVisible(true);
            decisionPane.setMouseTransparent(false);
        }
    }
    
    private void onDecide(Decision option) {
        SoundEffects.play("click");
        
        deciding = false;
        decisionPane.getChildren().clear();
        decisionPane.setVisible(false);
        decisionPane.setMouseTransparent(true);
        
        round.getGame().getBus().post(new UserDecideEvent(round, option));
    }
    
    @Nullable
    private PlacingPiece getCurrentPlacingPiece() {
        if (round.getGame().getInfo().getPlacingPieces().length == 0) return null;
        if (playersToPieceChooserGroups == null) return round.getGame().getInfo().getPlacingPieces()[0];
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
    
    @Subscribe
    public void onWin(WinEvent e) {
        Platform.runLater(() -> {
            try {
                Player winner = e.getWinner();
                if (winner == null) {
                    SoundEffects.play("lose");
                    winImg.setImage(new Image(new Resource("ca.keal.sastrane.icon", "draw.png").get().openStream()));
                    winText.setText(I18n.localize("gui.game.result.draw"));
                } else {
                    if (e.getRound().getPlayersToMovers().get(winner) instanceof AI
                            && !(e.getRound().getPlayersToMovers().values().stream().allMatch(p -> p instanceof AI))) {
                        SoundEffects.play("lose");
                    } else {
                        SoundEffects.play("win");
                    }
                    winImg.setImage(new Image(winner.getIcon().get().openStream()));
                    winText.setText(I18n.localize("gui.game.result.win", I18n.localize(winner.getI18nName())));
                }
                
                if (e.getRound().getGame().getInfo() instanceof Notatable) {
                    notationBtn.setVisible(true);
                }
                
                winPane.setMouseTransparent(false);
                winPane.setVisible(true);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberBinding winPaneMinWH = Bindings.min(game.widthProperty(), game.heightProperty()).multiply(0.6);
        winPane.prefWidthProperty().bind(winPaneMinWH);
        winPane.prefHeightProperty().bind(winPaneMinWH);
        
        winText.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font(boardGrid.heightProperty().divide(10).get()),
                boardGrid.heightProperty()
        ));
    }
    
    @FXML
    @Override
    protected void goBack(Event e) {
        super.goBack(e);
        GuiUtils.setTitleToDefault();
    }
    
    @FXML
    @SneakyThrows
    private void toNotation(ActionEvent e) {
        SoundEffects.play("click");
        
        FXMLLoader loader = GuiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "simple-text.fxml"));
        Scene previousScene = Main.getStage().getScene();
        Scene scene = GuiUtils.getScene((Parent) loader.load(), previousScene);
        
        SimpleTextController controller = loader.getController();
        controller.setTitle(I18n.localize("gui.game.over.notation"));
        controller.setText(((Notatable) round.getGame().getInfo()).getNotater().notate(round.getMoves()));
        controller.setPreviousScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"));
        controller.useMonospacedFont();
        
        Main.getStage().setScene(scene);
    }
    
}