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

package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.test.RoundConstructingTestFixture;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Test
public class MarkTest extends RoundConstructingTestFixture {
    
    private Mark mark;
    
    @BeforeMethod
    @Override
    public void before() {
        super.before();
        mark = new Mark();
    }
    
    public void getMoveAt_emptySquareOnEmptyBoard_correctMove() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("   ")
                .row("   ")
                .row("   "));
        Round round = new Round(game, playersToMovers);
        Square square = new Square(2, 1);
        
        PlacingMove move = mark.getMoveAt(square, round, player1);
        assertEquals(move, new PlacingMove(new OwnedPiece(mark, player1), square));
    }
    
    public void getMoveAt_emptySquareOnPartiallyFullBoard_correctMove() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("OXO")
                .row("OOX")
                .row(" XX")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Square square = new Square(0, 2);
    
        PlacingMove move = mark.getMoveAt(square, round, player1);
        assertEquals(move, new PlacingMove(new OwnedPiece(mark, player1), square));
    }
    
    public void getMoveAt_occupiedSquareOnPartiallyFullBoard_null() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("  X")
                .row("   ")
                .row("   ")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X)));
        Round round = new Round(game, playersToMovers);
        Square square = new Square(2, 0);
        
        PlacingMove move = mark.getMoveAt(square, round, player1);
        assertEquals(move, null);
    }
    
    public void getMoveAt_occupiedSquareOnFullBoard_null() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("OXO")
                .row("XXO")
                .row("XOX")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Square square = new Square(1, 1);
    
        PlacingMove move = mark.getMoveAt(square, round, player1);
        assertEquals(move, null);
    }
    
}