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

package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.piece.OwnedPiece;
import com.google.inject.Singleton;

import java.util.List;

/**
 * Notates Reversi games using a grid on which move numbers are recorded, à la 
 * <a href="http://usothello.org/trans/Transcripts_day1_2014.pdf">US Othello championship transcript</a>.
 */
@Singleton
class GridNotater implements Notater {
    
    private static final int NOTHING = 0;
    private static final int PRE_WHITE = -1;
    private static final int PRE_BLACK = -2;
    
    @Override
    public String notate(List<StateChange> moves) {
        Board start = moves.get(0).getBefore();
        
        int[][] grid = new int[start.getMaxY()+1][start.getMaxX()+1]; // grid[y][x]; it's initialized with 0 (NOTHING)
        for (Square square : start) {
            OwnedPiece atSquare = start.get(square);
            if (atSquare != null) {
                int pre;
                if (atSquare.getOwner() == ReversiPlayer.WHITE) {
                    pre = PRE_WHITE;
                } else {
                    pre = PRE_BLACK;
                }
                grid[square.getY()][square.getX()] = pre;
            }
        }
        
        for (int i = 0; i < moves.size(); i++) {
            Square square = moves.get(i).getMove().getEndPos();
            grid[square.getY()][square.getX()] = i + 1;
        }
        
        return gridToString(grid);
    }
    
    private String gridToString(int[][] grid) {
        String prePostfix = "   a  b  c  d  e  f  g  h";
        StringBuilder res = new StringBuilder(prePostfix);
        
        for (int y = 0; y < grid.length; y++) {
            res.append(System.lineSeparator());
            res.append(y + 1);
            
            for (int x = 0; x < grid[y].length; x++) {
                String squareRepr;
                switch (grid[y][x]) {
                    case NOTHING: {
                        squareRepr = ".";
                        break;
                    }
                    case PRE_WHITE: {
                        squareRepr = "○";
                        break;
                    }
                    case PRE_BLACK: {
                        squareRepr = "●";
                        break;
                    }
                    default: {
                        squareRepr = Integer.toString(grid[y][x]);
                    }
                }
                res.append(String.format("%1$3s", squareRepr)); // left pad to 3 chars with spaces
            }
            
            res.append(' ');
            res.append(y + 1);
        }
        
        return res.append(System.lineSeparator()).append(prePostfix).toString();
    }
    
}