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

package ca.keal.sastrane.go;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.BoardDecor;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.gui.GuiUtils;
import com.google.inject.Inject;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * A decoration representing the <a href="http://senseis.xmp.net/?StarPoint">star points</a>.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class StarPointsDecor extends BoardDecor {
    
    private final GuiUtils guiUtils;
    
    @Override
    public List<Square> getSquares() {
        return Arrays.asList(
                new Square(3, 3),  new Square(9, 3),  new Square(15, 3),
                new Square(3, 9),  new Square(9, 9),  new Square(15, 9),
                new Square(3, 15), new Square(9, 15), new Square(15, 15));
    }
    
    @Override
    public Node getDecor(Square square, Board board, GridPane grid) {
        NumberBinding cellSize = guiUtils.getCellSizeBinding(board, grid);
        
        Circle dot = new Circle();
        dot.setCenterX(0);
        dot.setCenterY(0);
        dot.radiusProperty().bind(cellSize.multiply(0.15));
        
        // Move the dot into the centre of the point
        NumberBinding offset = cellSize.divide(2).add(cellSize.multiply(0.073)).negate();
        dot.translateXProperty().bind(offset);
        dot.translateYProperty().bind(offset);
        
        return dot;
    }
    
}