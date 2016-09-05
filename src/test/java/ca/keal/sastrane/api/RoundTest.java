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

import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import com.google.common.eventbus.EventBus;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

@Test
public class RoundTest {
    
    @Mock private Player player1, player2;
    @Mock private Mover mover1, mover2;
    private Map<Player, Mover> playersToMovers;
    
    @Mock private Board board;
    @Mock private Board.Factory boardFactory;
    
    @Mock private Arbitrator arbitrator;
    @Mock private Notater notater;
    
    @BeforeMethod(alwaysRun = true)
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        playersToMovers = new HashMap<>();
        playersToMovers.put(player1, mover1);
        playersToMovers.put(player2, mover2);
        
        when(boardFactory.bus(any())).thenReturn(boardFactory);
        when(boardFactory.build()).thenReturn(board);
    }
    
    // Constructor
    
    public void constructor_withGame_setsGame() {
        Game game = mock(Game.class);
        Round round = new Round(game, playersToMovers);
        assertEquals(round.getGame(), game);
    }
    
    public void constructor_withPlayersToMovers_setsPlayersToMovers() {
        Round round = new Round(mock(Game.class), playersToMovers);
        assertEquals(round.getPlayersToMovers(), playersToMovers);
    }
    
    public void constructor_withBoardFactory_setsBoardBuiltByFactory() {
        Game game = mock(Game.class);
        when(game.getBoardFactory()).thenReturn(boardFactory);
        Round round = new Round(game, playersToMovers);
        assertEquals(round.getBoard(), board);
    }
    
    public void constructor_withName_setsEventBusWithName() {
        String name = "__TEST__";
        Game game = mock(Game.class);
        when(game.getName()).thenReturn(name);
        Round round = new Round(game, playersToMovers);
        assertEquals(round.getBus().identifier(), name);
    }
    
    public void constructor_withDefaultsRegistrator_callsItOnce() {
        Game game = mock(Game.class);
        Consumer<EventBus> defaultsRegistrator = bus -> {};
        when(game.getDefaultsRegistrator()).thenReturn(defaultsRegistrator);
        verify(defaultsRegistrator, times(1)).accept(any());
    }
    
    // Copy constructor
    
    public void copyConstructor_copies() {
        Round round = new Round(mock(Game.class), playersToMovers);
        Round copy = new Round(round);
        assertFalse(round == copy); // not returning the same object
        assertEquals(round, copy);
    }
    
    // willAutoPass()
    
    public void willAutoPass_withAutoPassFalseAndMovesLeft_returnsFalse() {
        MovingPiece piece = mock(MovingPiece.class);
        when(piece.getPossibleMoves(any(), any(), any()))
                .thenReturn(Collections.singletonList(new MovingMove(new Square(0, 0), new Square(1, 0))));
        
        Game game = mock(Game.class);
        when(game.getBoardFactory()).thenReturn(Board.factory()
                .row("a ")
                .piece('a', new OwnedPieceFactory(() -> piece, player1)));
        
        Round round = new Round(game, playersToMovers);
        assertFalse(round.willAutoPass(player1));
    }
    
    // TODO more tests...
    
}