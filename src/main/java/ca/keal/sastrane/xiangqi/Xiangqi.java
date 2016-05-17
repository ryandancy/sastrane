package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.event.ToGameEvent;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static ca.keal.sastrane.gui.GuiUtils.lookup;

public class Xiangqi extends Game {
    
    @Getter private static final Xiangqi instance = new Xiangqi();
    
    public Xiangqi() {
        super("ca.keal.sastrane.xiangqi.i18n.xiangqi", "xiangqi.name",
                new Resource("ca.keal.sastrane.xiangqi", "xiangqi.png"),
                new Resource("ca.keal.sastrane.xiangqi", "xiangqi.css"),
                XiangqiPlayer.values(), XiangqiAI::new,
                Board.factory()
                        .row("RHEAGAEHR")
                        .row("         ")
                        .row(" C     C ")
                        .row("S S S S S")
                        .row("         ")
                        .row("         ")
                        .row("s s s s s")
                        .row(" c     c ")
                        .row("         ")
                        .row("rheagaehr")
                        .piece('R', new OwnedPieceFactory(Chariot::new, XiangqiPlayer.BLACK))
                        .piece('H', new OwnedPieceFactory(Horse::new, XiangqiPlayer.BLACK))
                        .piece('E', new OwnedPieceFactory(Elephant::new, XiangqiPlayer.BLACK))
                        .piece('A', new OwnedPieceFactory(Advisor::new, XiangqiPlayer.BLACK))
                        .piece('G', new OwnedPieceFactory(General::new, XiangqiPlayer.BLACK))
                        .piece('C', new OwnedPieceFactory(Cannon::new, XiangqiPlayer.BLACK))
                        .piece('S', new OwnedPieceFactory(Soldier::new, XiangqiPlayer.BLACK))
                        .piece('r', new OwnedPieceFactory(Chariot::new, XiangqiPlayer.RED))
                        .piece('h', new OwnedPieceFactory(Horse::new, XiangqiPlayer.RED))
                        .piece('e', new OwnedPieceFactory(Elephant::new, XiangqiPlayer.RED))
                        .piece('a', new OwnedPieceFactory(Advisor::new, XiangqiPlayer.RED))
                        .piece('g', new OwnedPieceFactory(General::new, XiangqiPlayer.RED))
                        .piece('c', new OwnedPieceFactory(Cannon::new, XiangqiPlayer.RED))
                        .piece('s', new OwnedPieceFactory(Soldier::new, XiangqiPlayer.RED)));
    }
    
    @Override
    public Result getResult(Round round) {
        Player player = XiangqiPlayer.RED;
        Player opponent = XiangqiPlayer.BLACK;
        for (int i = 0; i < 2; i++) {
            if (round.getAllPossibleMoves(player).size() == 0) {
                return new Result.Win(opponent);
            }
            
            Player temp = player;
            player = opponent;
            opponent = temp;
        }
        return Result.NOT_OVER;
    }
    
    @Override
    protected void registerDefaults(EventBus bus) {
        bus.register(this);
    }
    
    @Subscribe
    public void afterGameScreenLoaded(ToGameEvent.Post e) {
        GridPane boardGrid = (GridPane) e.getGameScene().lookup("#board");
        
        for (XiangqiPlayer player : XiangqiPlayer.values()) {
            List<StackPane> palace = player.getPalace().stream()
                    .map(s -> (StackPane) lookup(e.getGameScene(), s))
                    .collect(Collectors.toList());
            
            boardGrid.getChildren().add(getNEToSWLine(palace.get(3)));
            boardGrid.getChildren().add(getNWToSELine(palace.get(4)));
            boardGrid.getChildren().add(getNWToSELine(palace.get(6)));
            boardGrid.getChildren().add(getNEToSWLine(palace.get(7)));
        }
    }
    
    private Line getNWToSELine(StackPane square) {
        Region overlay = (Region) square.lookup(".overlay");
        Line line = new Line();
        line.getStyleClass().add("decor");
        line.startXProperty().bind(square.layoutXProperty());
        line.startYProperty().bind(square.layoutYProperty());
        line.endXProperty().bind(square.layoutXProperty().add(overlay.minWidthProperty()));
        line.endYProperty().bind(square.layoutYProperty().add(overlay.minHeightProperty()));
        return line;
    }
    
    private Line getNEToSWLine(StackPane square) {
        Region overlay = (Region) square.lookup(".overlay");
        Line line = new Line();
        line.getStyleClass().add("decor");
        line.startXProperty().bind(square.layoutXProperty().add(overlay.minWidthProperty()));
        line.startYProperty().bind(square.layoutYProperty());
        line.endXProperty().bind(square.layoutXProperty());
        line.endYProperty().bind(square.layoutYProperty().add(overlay.minHeightProperty()));
        return line;
    }
    
}