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
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.test.RoundConstructingTestFixture;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Test
public class TicTacToeArbitratorTest extends RoundConstructingTestFixture {
    
    private TicTacToeArbitrator arbitrator;
    
    @BeforeMethod
    @Override
    public void before() {
        super.before();
        arbitrator = new TicTacToeArbitrator();
    }
    
    public void arbitrate_xxx_nnn_nnn_x() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("XXX")
                .row("   ")
                .row("   ")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.X);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nnn_ooo_nnn_o() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("   ")
                .row("OOO")
                .row("   ")
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.O);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nnn_nnn_xxx_x() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("   ")
                .row("   ")
                .row("XXX")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.X);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_onn_onn_onn_o() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("O  ")
                .row("O  ")
                .row("O  ")
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.O);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nxn_nxn_nxn_x() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row(" X ")
                .row(" X ")
                .row(" X ")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.X);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nno_nno_nno_o() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("  O")
                .row("  O")
                .row("  O")
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.O);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_xnn_nxn_nnx_x() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("X  ")
                .row(" X ")
                .row("  X")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.X);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nno_non_onn_o() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("  O")
                .row(" O ")
                .row("O  ")
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.O);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_xxo_xox_xxo_x() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("XXO")
                .row("XOX")
                .row("XXO")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = new Result.Win(TicTacToePlayer.X);
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_xox_oxo_oxo_draw() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("XOX")
                .row("OXO")
                .row("OXO")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.DRAW;
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_oox_xoo_oxx_draw() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("OOX")
                .row("XOO")
                .row("OXX")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.DRAW;
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nxn_nxn_non_notOver() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row(" X ")
                .row(" X ")
                .row(" O ")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.NOT_OVER;
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_oon_noo_nxx_notOver() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("OO ")
                .row(" OO")
                .row(" XX")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.NOT_OVER;
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_nxn_nnx_xnn_notOver() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row(" X ")
                .row("  X")
                .row("X  ")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X)));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.NOT_OVER;
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_onx_oox_xnn_notOver() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("O X")
                .row("OOX")
                .row("X  ")
                .piece('X', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.X))
                .piece('O', new OwnedPieceFactory(() -> mock(Mark.class), TicTacToePlayer.O)));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.NOT_OVER;
        
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
    public void arbitrate_empty_notOver() {
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("   ")
                .row("   ")
                .row("   "));
        Round round = new Round(game, playersToMovers);
        Result expected = Result.NOT_OVER;
    
        assertEquals(arbitrator.arbitrate(round), expected);
    }
    
}