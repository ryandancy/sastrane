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
import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

/**
 * A class that hooks onto {@link ca.keal.sastrane.api.event.ToGameEvent.Post} in order to place the diagonal lines that
 * go on the palace. FIXME Completely bugged, lines aren't aligned right and they're on top of the pieces
 */
public class PalaceLines {
    
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
    
}