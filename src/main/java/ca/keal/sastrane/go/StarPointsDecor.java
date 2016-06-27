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
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * A decoration representing the <a href="http://senseis.xmp.net/?StarPoint">star points</a>.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class StarPointsDecor extends BoardDecor {
    
    private final GuiUtils guiUtils;
    
    @Override
    public List<Square> getSquares() {
        return Arrays.asList(
                new Square(4, 4),  new Square(10, 4),  new Square(14, 4),
                new Square(4, 10), new Square(10, 10), new Square(14, 10),
                new Square(4, 14), new Square(10, 14), new Square(14, 14));
    }
    
    @Override
    public Node getDecor(Square square, Board board, GridPane grid) {
        NumberBinding dotSize = guiUtils.getCellSizeBinding(board, grid).divide(10);
        
        Circle dot = new Circle();
        dot.setCenterX(0);
        dot.setCenterY(0);
        dot.radiusProperty().bind(dotSize);
        
        // Move the dot into the centre of the point
        NumberBinding offset = dotSize.divide(2).negate();
        dot.translateXProperty().bind(offset);
        dot.translateYProperty().bind(offset);
        
        return dot;
    }
    
}