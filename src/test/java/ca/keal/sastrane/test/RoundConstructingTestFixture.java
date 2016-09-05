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

package ca.keal.sastrane.test;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.piece.PlacingPiece;
import com.google.common.eventbus.EventBus;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A test fixture superclass for fixtures that construct {@link Round}s frequently. It provides convenient access to
 * its dependencies.
 */
public class RoundConstructingTestFixture {
    
    @Mock protected Player player1, player2;
    @Mock protected Mover mover1, mover2;
    protected Map<Player, Mover> playersToMovers;
    
    @Mock protected Board board;
    @Mock protected Board.Factory boardFactory;
    
    @Mock protected Game game;
    
    @SuppressWarnings("unchecked")
    protected Consumer<EventBus> defaultsRegistrator = mock(Consumer.class);
    
    protected static final String NAME = "__TEST__";
    
    @BeforeMethod(alwaysRun = true)
    public void before() {
        MockitoAnnotations.initMocks(this);
    
        playersToMovers = new HashMap<>();
        playersToMovers.put(player1, mover1);
        playersToMovers.put(player2, mover2);
    
        when(boardFactory.bus(any())).thenReturn(boardFactory);
        when(boardFactory.build()).thenReturn(board);
    
        when(game.getBoardFactory()).thenReturn(boardFactory);
        when(game.getPlayers()).thenReturn(new Player[] {player1, player2});
        when(game.getName()).thenReturn(NAME);
        when(game.getDefaultsRegistrator()).thenReturn(defaultsRegistrator);
        when(game.getPlacingPieces()).thenReturn(new PlacingPiece[0]);
    }
    
}