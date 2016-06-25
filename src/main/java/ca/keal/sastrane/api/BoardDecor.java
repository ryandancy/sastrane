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

package ca.keal.sastrane.api;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Represents a decoration on the board, e.g. the diagonal lines of the palace in xiangqi.
 */
public abstract class BoardDecor implements Comparable<BoardDecor> {
    
    /** If there are multiple decors on the same square, override this to take control of the order of decors. */
    protected int getOrder() {
        return 0;
    }
    
    @Override
    public int compareTo(BoardDecor other) {
        return Integer.compare(getOrder(), other.getOrder());
    }
    
    public abstract List<Square> getSquares();
    
    public abstract Node getDecor(Square square, Board board, GridPane grid);
    
}