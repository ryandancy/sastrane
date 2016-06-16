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

package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Notatable;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.ToGameEvent;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import lombok.Getter;

public class Xiangqi extends Game implements Notatable {
    
    public static final int MAXX = 9;
    public static final int MAXY = 10;
    
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
            line(e.getRound(), boardGrid, player.getPalace().get(3), DiagLineDirection.NE_SW);
            line(e.getRound(), boardGrid, player.getPalace().get(4), DiagLineDirection.NW_SE);
            line(e.getRound(), boardGrid, player.getPalace().get(6), DiagLineDirection.NW_SE);
            line(e.getRound(), boardGrid, player.getPalace().get(7), DiagLineDirection.NE_SW);
        }
    }
    
    private enum DiagLineDirection {NW_SE, NE_SW}
    
    private void line(Round round, GridPane grid, Square square, DiagLineDirection dir) {
        Line line = new Line();
        NumberBinding cellDimen = Bindings.min(grid.widthProperty().divide(round.getBoard().getMaxX() + 1),
                grid.heightProperty().divide(round.getBoard().getMaxY() + 1)).add(10); // DRY???
        
        line.getStyleClass().add("decor");
        line.setStartY(0);
        if (dir == DiagLineDirection.NW_SE) {
            line.setStartX(0);
            line.endXProperty().bind(cellDimen);
            line.endYProperty().bind(cellDimen);
        } else { // NE_SW
            line.startXProperty().bind(cellDimen);
            line.setEndX(0);
            line.endYProperty().bind(cellDimen);
        }
        
        grid.add(line, square.getX(), square.getY());
    }
    
    @Override
    public Notater getNotater() {
        return new WXFNotater();
    }
    
}