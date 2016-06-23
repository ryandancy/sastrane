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

import ca.keal.sastrane.api.event.MoveEvent;
import ca.keal.sastrane.api.event.RoundEvent;
import ca.keal.sastrane.api.event.TurnEvent;
import ca.keal.sastrane.api.event.WinEvent;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Utils;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Round {
    
    @Getter private final String gameID;
    @Getter private final Map<Player, Mover> playersToMovers;
    @Getter private final Board board;
    @Getter private EventBus bus;
    
    @Getter private int moveNum = 0;
    @Getter private boolean ended = false;
    
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Getter private List<StateChange> moves = new ArrayList<>();
    
    private final Map<String, Player[]> players;
    private final Map<String, PlacingPiece[]> placingPieces;
    private final Map<String, Arbitrator> arbitrators;
    private final Map<String, Notater> notaters;
    
    @Inject
    public Round(@Assisted String gameID, @Assisted Map<Player, Mover> playersToMovers,
                 @GameAttribute(GameAttr.NAME) Map<String, String> names,
                 @GameAttribute(GameAttr.BOARD_FACTORY) Map<String, Board.Factory> boardFactories,
                 @GameAttribute(GameAttr.DEFAULTS_REGISTRATOR) Map<String, Consumer<EventBus>> defaultsRegistrators,
                 @GameAttribute(GameAttr.PLAYERS) Map<String, Player[]> players,
                 @GameAttribute(GameAttr.PLACING_PIECES) Map<String, PlacingPiece[]> placingPieces,
                 @GameAttribute(GameAttr.ARBITRATOR) Map<String, Arbitrator> arbitrators,
                 @GameAttribute(GameAttr.NOTATER) Map<String, Notater> notaters) {
        if (!Utils.areElementsEqual(playersToMovers.keySet(), Arrays.asList(players.get(gameID)))) {
            throw new IllegalArgumentException("Round: playersToMovers.keySet() must = game.getCombatants()");
        }
        this.gameID = gameID;
        this.playersToMovers = ImmutableMap.copyOf(playersToMovers);
        bus = new EventBus(names.get(gameID));
        defaultsRegistrators.get(gameID).accept(bus);
        this.board = boardFactories.get(gameID).bus(bus).build();
        
        this.players = players;
        this.placingPieces = placingPieces;
        this.arbitrators = arbitrators;
        this.notaters = notaters;
    }
    
    // TODO make this a static method and use the factory
    public Round(Round round) {
        this(round.gameID, ImmutableMap.copyOf(round.playersToMovers), new Board(round.board), round.bus, round.moveNum,
                round.ended, new ArrayList<>(round.moves), round.players, round.placingPieces, round.arbitrators,
                round.notaters);
    }
    
    public void nextTurn() {
        if (ended) throw new IllegalStateException("nextTurn() cannot be called on a Round that is already ended");
        bus.post(new TurnEvent.Pre(this));
        
        Player player = getCurrentTurn();
        Mover mover = playersToMovers.get(player);
        Move move = mover.getMove(this, player);
        
        Board oldBoard = new Board(board);
        
        bus.post(new MoveEvent.Pre(this, move));
        move.move(board);
        bus.post(new MoveEvent.Post(this, move));
        
        moves.add(new StateChange(oldBoard, move, new Round(this)));
        
        bus.post(new TurnEvent.Post(this));
        
        Result result = arbitrators.get(gameID).arbitrate(this);
        if (result != Result.NOT_OVER) {
            ended = true;
    
            Notater notater = notaters.get(gameID);
            String notation = notater == null ? null : notater.notate(moves); // notater == null -> not notatable
            
            bus.post(new WinEvent(this, result, notation));
        }
        
        moveNum++;
    }
    
    public void start() {
        bus.post(new RoundEvent.Pre(this));
        while (!ended) {
            nextTurn();
        }
        bus.post(new RoundEvent.Post(this));
    }
    
    public Player getCurrentTurn() {
        return players.get(gameID)[moveNum % players.get(gameID).length];
    }
    
    public Round copyWithMove(Move move) {
        Round newRound = new Round(this);
        move.move(newRound.getBoard());
        return newRound;
    }
    
    public List<Move> getAllPossibleMoves(Player player) {
        List<Move> moves = new ArrayList<>();
        for (Square square : board) {
            OwnedPiece atSquare = board.get(square);
            if (atSquare != null && atSquare.getPiece() instanceof MovingPiece && atSquare.getOwner() == player) {
                moves.addAll(((MovingPiece) atSquare.getPiece()).getPossibleMoves(this, square, atSquare.getOwner()));
            }
        }
        for (PlacingPiece placingPiece : placingPieces.get(gameID)) {
            moves.addAll(placingPiece.getPossiblePlacements(this, player));
        }
        return moves;
    }
    
    public StateChange getLastMove() {
        return getLastMove(0);
    }
    
    public StateChange getLastMove(int n) {
        return moves.get(moves.size() - 1 - n);
    }
    
    /** For AssistedInject */
    public interface Factory {
        Round create(String gameID, Map<Player, Mover> playersToMovers);
    }
    
}