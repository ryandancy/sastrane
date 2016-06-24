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

import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.ToGameEvent;
import ca.keal.sastrane.gui.GuiUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import lombok.RequiredArgsConstructor;

/**
 * A class that hooks onto {@link ca.keal.sastrane.api.event.ToGameEvent.Post} in order to place the diagonal lines that
 * go on the palace. FIXME Completely bugged, lines aren't aligned right and they're on top of the pieces
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PalaceLines {
    
    private final GuiUtils guiUtils;
    
    @Subscribe
    public void afterGameScreenLoaded(ToGameEvent.Post e) {
        GridPane boardGrid = (GridPane) e.getGameScene().lookup("#board");
        
        for (XiangqiPlayer player : XiangqiPlayer.values()) {
            line(e.getRound(), e.getGameScene(), boardGrid, player.getPalace().get(3), DiagLineDirection.NE_SW);
            line(e.getRound(), e.getGameScene(), boardGrid, player.getPalace().get(4), DiagLineDirection.NW_SE);
            line(e.getRound(), e.getGameScene(), boardGrid, player.getPalace().get(6), DiagLineDirection.NW_SE);
            line(e.getRound(), e.getGameScene(), boardGrid, player.getPalace().get(7), DiagLineDirection.NE_SW);
        }
    }
    
    private enum DiagLineDirection {NW_SE, NE_SW}
    
    private void line(Round round, Scene scene, GridPane grid, Square square, DiagLineDirection dir) {
        Line line = new Line();
        NumberBinding cellDimen = Bindings.min(grid.widthProperty().divide(round.getBoard().getMaxX() + 1),
                grid.heightProperty().divide(round.getBoard().getMaxY() + 1)).add(12); // DRY???
        
        line.getStyleClass().add("decor");
        if (dir == DiagLineDirection.NW_SE) {
            line.setStartX(0);
            line.setStartY(0);
            line.endXProperty().bind(cellDimen);
            line.endYProperty().bind(cellDimen);
        } else { // NE_SW
            line.startXProperty().bind(cellDimen.subtract(0.5));
            line.setStartY(-0.5);
            line.setEndX(-0.5);
            line.endYProperty().bind(cellDimen.subtract(0.5));
        }
    
        Node squareNode = guiUtils.getNodeFromGridPane(grid, square.getX(), square.getY());
        assert squareNode != null;
        line.translateXProperty().bind(cellDimen.subtract(9).divide(2).add(Bindings.createDoubleBinding(
                () -> squareNode.localToScene(squareNode.getBoundsInLocal()).getMinX(),
                squareNode.boundsInLocalProperty())));
        line.translateYProperty().bind(cellDimen.subtract(9).divide(2).add(Bindings.createDoubleBinding(
                () -> squareNode.localToScene(squareNode.getBoundsInLocal()).getMinY(),
                squareNode.boundsInLocalProperty())));
        ((Pane) scene.getRoot()).getChildren().add(line);
    }
    
}