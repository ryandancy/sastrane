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

package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Utils;
import com.google.common.collect.Multiset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notates chess using <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)#Long_algebraic_notation">long 
 * algebraic notation</a>.
 */
public class LongAlgebraicNotater implements Notater {
    
    private static final Map<Class<? extends Piece>, String> PIECE_CHARS = new HashMap<>();
    
    static {
        PIECE_CHARS.put(Pawn.class, "");
        PIECE_CHARS.put(Knight.class, "N");
        PIECE_CHARS.put(Bishop.class, "B");
        PIECE_CHARS.put(Rook.class, "R");
        PIECE_CHARS.put(Queen.class, "Q");
        PIECE_CHARS.put(King.class, "K");
    }
    
    @Override
    public String notate(List<StateChange> moves) {
        StringBuilder res = new StringBuilder();
        
        int moveNum = 1;
        boolean isFirst = true;
        boolean startNew = true;
        for (StateChange change : moves) {
            if (startNew) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    res.append(System.lineSeparator());
                }
                res.append(moveNum++);
                res.append(". ");
            } else {
                res.append(' ');
            }
            startNew = !startNew;
            
            MovingMove move = change.getMovingMove();
            
            // Castling - kingside (right) = "0-0", queenside (left) = "0-0-0"
            // A >1 square move by the king is a castling
            if (change.getMovedPiece().getPiece() instanceof King) {
                int deltaX = move.getTo().getX() - move.getFrom().getX();
                if (Math.abs(deltaX) > 1) {
                    if (deltaX < 0) {
                        // Queenside castle
                        res.append("0-0-0");
                    } else if (deltaX > 0) {
                        // Kingside castle
                        res.append("0-0");
                    }
                    appendSuffixes(change.getAfterRound(), change.getMovedPiece().getOwner(), res);
                    continue;
                }
            }
            
            // Moving/capturing
            res.append(PIECE_CHARS.get(change.getMovedPiece().getPiece().getClass()));
            appendSquareID(move.getFrom(), res);
            
            Multiset<Player> beforeCount = Utils.countPlayers(change.getBefore());
            Multiset<Player> afterCount = Utils.countPlayers(change.getAfter());
            Player notMover = change.getMovedPiece().getOwner() == ChessPlayer.WHITE ? ChessPlayer.BLACK
                    : ChessPlayer.WHITE;
            
            if (afterCount.count(notMover) < beforeCount.count(notMover)) {
                // capture - e.g. Be6xf7
                res.append('x');
                appendSquareID(move.getTo(), res);
                
                if (change.getBefore().get(move.getTo()) == null) {
                    // en passent - e.p. suffix
                    res.append("e.p.");
                }
            } else {
                // move - e.g. Be6-f7
                res.append('-');
                appendSquareID(move.getTo(), res);
                
                if (change.getMovedPiece().getPiece() instanceof Pawn) {
                    OwnedPiece pieceAfter = change.getAfter().get(move.getTo());
                    if (pieceAfter != null && !(pieceAfter.getPiece() instanceof Pawn)) {
                        // pawn promotion - e.g. e7-e8=Q
                        res.append('=');
                        res.append(PIECE_CHARS.get(pieceAfter.getPiece().getClass()));
                    }
                }
            }
            
            appendSuffixes(change.getAfterRound(), change.getMovedPiece().getOwner(), res);
        }
        
        // game end: 1-0 for white wins, 0-1 black wins, 1/2-1/2 tie
        Result result = ChessArbitrator.INSTANCE.arbitrate(moves.get(moves.size() - 1).getAfterRound()); // TODO DI here
        if (result instanceof Result.Win) {
            res.append(System.lineSeparator());
            if (((Result.Win) result).getPlayer() == ChessPlayer.WHITE) {
                res.append("1-0");
            } else { // black
                res.append("0-1");
            }
        } else if (result == Result.DRAW) {
            res.append(System.lineSeparator());
            res.append("½-½");
        }
        
        return res.toString();
    }
    
    private void appendSquareID(Square square, StringBuilder res) {
        res.append((char) (square.getX() + 'a'));
        res.append(8 - square.getY());
    }
    
    private void appendSuffixes(Round afterRound, Player otherPlayer, StringBuilder res) {
        Player player = otherPlayer == ChessPlayer.WHITE ? ChessPlayer.BLACK : ChessPlayer.WHITE;
        if (KingInCheckUtils.isKingInCheck(afterRound, player)) {
            if (afterRound.getAllPossibleMoves(player).size() == 0) {
                // checkmate
                res.append('#');
            } else {
                // check
                res.append('+');
            }
        }
    }
    
}