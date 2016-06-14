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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Round {
    
    private final Game game;
    private final Map<Player, Mover> playersToMovers;
    private final Board board;
    
    private int moveNum = 0;
    private boolean ended = false;
    
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<StateChange> moves = new ArrayList<>();
    
    public Round(Game game, Map<Player, Mover> playersToMovers) {
        if (!Utils.areElementsEqual(playersToMovers.keySet(), Arrays.asList(game.getPlayers()))) {
            throw new IllegalArgumentException("Round: playersToMovers.keySet() must = game.getCombatants()");
        }
        this.game = game;
        this.playersToMovers = ImmutableMap.copyOf(playersToMovers);
        this.board = game.getBoardFactory().build();
    }
    
    public Round(Round round) {
        this(round.getGame(), ImmutableMap.copyOf(round.getPlayersToMovers()), new Board(round.getBoard()),
                round.getMoveNum(), round.isEnded(), new ArrayList<>(round.getMoves()));
    }
    
    public void nextTurn() {
        if (ended) throw new IllegalStateException("nextTurn() cannot be called on a Round that is already ended");
        game.getBus().post(new TurnEvent.Pre(this));
        
        Player player = getCurrentTurn();
        Mover mover = playersToMovers.get(player);
        Move move = mover.getMove(this, player);
        
        Board oldBoard = new Board(board);
        
        game.getBus().post(new MoveEvent.Pre(this, move));
        move.move(board);
        game.getBus().post(new MoveEvent.Post(this, move));
        
        moves.add(new StateChange(oldBoard, move, new Round(this)));
        
        game.getBus().post(new TurnEvent.Post(this));
        
        Result result = game.getResult(this);
        if (result != Result.NOT_OVER) {
            ended = true;
            
            String notation;
            if (game instanceof Notatable) {
                notation = ((Notatable) game).getNotater().notate(moves);
            } else {
                notation = null;
            }
            
            game.getBus().post(new WinEvent(this, result, notation));
        }
        
        moveNum++;
    }
    
    public void start() {
        game.getBus().post(new RoundEvent.Pre(this));
        while (!ended) {
            nextTurn();
        }
        game.getBus().post(new RoundEvent.Post(this));
        game.refreshBus();
    }
    
    public Player getCurrentTurn() {
        return game.getPlayers()[moveNum % game.getPlayers().length];
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
        for (PlacingPiece placingPiece : game.getPlacingPieces()) {
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
    
}