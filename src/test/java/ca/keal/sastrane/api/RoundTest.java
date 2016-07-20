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
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.test.TestUtils;
import com.google.common.eventbus.EventBus;
import lombok.val;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
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
    
    private final String name = TestUtils.TEST_GAMEID;
    
    @Mock private Board board;
    @Mock private Board.Factory boardFactory;
    
    private final Consumer<EventBus> defaultsRegistrator = e -> {};
    private Player[] players;
    
    @Mock private PlacingPiece piece;
    private PlacingPiece[] placingPieces;
    
    @Mock private Arbitrator arbitrator;
    @Mock private Notater notater;
    private Boolean autoPassEnabled;
    
    private Round round;
    
    @BeforeMethod(alwaysRun = true)
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        playersToMovers = new HashMap<>();
        playersToMovers.put(player1, mover1);
        playersToMovers.put(player2, mover2);
        
        when(boardFactory.bus(any())).thenReturn(boardFactory);
        when(boardFactory.build()).thenReturn(board);
        
        players = new Player[] {player1, player2};
        placingPieces = new PlacingPiece[] {piece};
        autoPassEnabled = false;
        
        round = new Round(
                TestUtils.TEST_GAMEID, playersToMovers, TestUtils.getInjectedMapAsTest(name),
                TestUtils.getInjectedMapAsTest(boardFactory), TestUtils.getInjectedMapAsTest(defaultsRegistrator),
                TestUtils.getInjectedMapAsTest(players), TestUtils.getInjectedMapAsTest(placingPieces),
                TestUtils.getInjectedMapAsTest(arbitrator), TestUtils.getInjectedMapAsTest(notater),
                TestUtils.getInjectedMapAsTest(autoPassEnabled));
    }
    
    @SuppressWarnings("ConstantConditions")
    private Round getRoundWith(@Nullable String gameID, @Nullable Map<Player, Mover> playersToMovers,
                               @Nullable String name, @Nullable Board.Factory boardFactory,
                               @Nullable Consumer<EventBus> defaultsRegistrator, @Nullable Player[] players,
                               @Nullable PlacingPiece[] placingPieces, @Nullable Arbitrator arbitrator,
                               @Nullable Notater notater, @Nullable Boolean autoPassEnabled) {
        if (gameID == null) gameID = TestUtils.TEST_GAMEID;
        if (playersToMovers == null) playersToMovers = this.playersToMovers;
        if (name == null) name = this.name;
        if (boardFactory == null) boardFactory = this.boardFactory;
        if (defaultsRegistrator == null) defaultsRegistrator = this.defaultsRegistrator;
        if (players == null) players = this.players;
        if (placingPieces == null) placingPieces = this.placingPieces;
        if (arbitrator == null) arbitrator = this.arbitrator;
        if (notater == null) notater = this.notater;
        if (autoPassEnabled == null) autoPassEnabled = this.autoPassEnabled;
        
        return new Round(
            gameID, playersToMovers, TestUtils.getInjectedMapAsTest(name),
            TestUtils.getInjectedMapAsTest(boardFactory), TestUtils.getInjectedMapAsTest(defaultsRegistrator),
            TestUtils.getInjectedMapAsTest(players), TestUtils.getInjectedMapAsTest(placingPieces),
            TestUtils.getInjectedMapAsTest(arbitrator), TestUtils.getInjectedMapAsTest(notater),
            TestUtils.getInjectedMapAsTest(autoPassEnabled));
    }
    
    // Constructor
    
    public void constructor_withGameID_setsGameID() {
        assertEquals(round.getGameID(), TestUtils.TEST_GAMEID);
    }
    
    public void constructor_withPlayersToMovers_setsPlayersToMovers() {
        assertEquals(round.getPlayersToMovers(), playersToMovers);
    }
    
    public void constructor_withBoardFactory_setsBoardBuiltByFactory() {
        assertEquals(round.getBoard(), board);
    }
    
    public void constructor_withName_setsEventBusWithName() {
        assertEquals(round.getBus().identifier(), name);
    }
    
    @SuppressWarnings("unchecked")
    public void constructor_withDefaultsRegistrator_callsIt() {
        Consumer<EventBus> defaultsRegistrator = mock(Consumer.class);
        getRoundWith(null, null, null, null, defaultsRegistrator, null, null, null, null, null);
        verify(defaultsRegistrator, times(1)).accept(any());
    }
    
    // Copy constructor
    
    public void copyConstructor_copies() {
        Round copy = new Round(round);
        assertFalse(round == copy); // not returning the same object
        assertEquals(round, copy);
    }
    
    // willAutoPass()
    
    public void willAutoPass_withAutoPassFalseAndMovesLeft_returnsFalse() {
        MovingPiece piece = mock(MovingPiece.class);
        when(piece.getPossibleMoves(any(), any(), any()))
                .thenReturn(Collections.singletonList(new MovingMove(new Square(0, 0), new Square(1, 0))));
        
        val round = getRoundWith(null, null, null, Board.factory()
                    .row("a ")
                    .piece('a', new OwnedPieceFactory(() -> piece, player1)),
                null, null, null, null, null, false);
        
        assertFalse(round.willAutoPass(player1));
    }
    
    // TODO more tests...
    
}