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
import ca.keal.sastrane.api.BoardDecor;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A decoration class that places the diagonal lines of the xiangqi palace.
 */
public class PalaceLinesDecor extends BoardDecor {
    
    @Override
    public List<Square> getSquares() {
        return Arrays.stream(XiangqiPlayer.values())
                .map(p -> Utils.selectIndexes(p.getPalace(), 3, 4, 6, 7))
                .collect(ArrayList::new, List::addAll, List::addAll); // flatten the list
    }
    
    @Override
    public Node getDecor(Square square, Board board, GridPane grid) {
        boolean isTopRank = square.getY() == 1 || square.getY() == board.getMaxY() - 1;
        boolean isLeftFile = square.getX() <= (double) (board.getMaxX() + 1) / 2.0 - 1;
        
        DiagLineDirection dir;
        if (isTopRank) {
            dir = isLeftFile ? DiagLineDirection.NE_SW : DiagLineDirection.NW_SE;
        } else {
            dir = isLeftFile ? DiagLineDirection.NW_SE : DiagLineDirection.NE_SW;
        }
        
        return line(board, grid, dir);
    }
    
    private enum DiagLineDirection {NW_SE, NE_SW}
    
    private Line line(Board board, GridPane grid, DiagLineDirection dir) {
        Line line = new Line();
        NumberBinding cellDimen = Bindings.min(grid.widthProperty().divide(board.getMaxX() + 1),
                grid.heightProperty().divide(board.getMaxY() + 1)).add(10); // DRY???
        
        line.getStyleClass().add("decor");
        if (dir == DiagLineDirection.NW_SE) {
            line.setStartX(0);
            line.setStartY(0);
            line.endXProperty().bind(cellDimen);
            line.endYProperty().bind(cellDimen);
        } else { // NE_SW
            line.startXProperty().bind(cellDimen);
            line.setStartY(0);
            line.setEndX(0);
            line.endYProperty().bind(cellDimen);
        }
        
        line.setTranslateX(1);
        line.setTranslateY(1);
        
        return line;
    }
    
}