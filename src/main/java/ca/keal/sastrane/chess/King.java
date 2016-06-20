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

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.MoveCountingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.util.Utils;
import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class King implements RecursiveMovingPiece, MoveCountingPiece {
    
    private int numMoves = 0;
    
    King(EventBus bus) {
        bus.register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        // All 8 surrounding squares that aren't in the path of any player other than player
        // It'd be a shame to ruin this beautifullly formatted piece of code, Intellij formatter: @formatter:off
        return Stream.concat(
                    getPossibleMovesNonRecursive(round, boardPos, player).stream(),
                    getCastlingMoves(round, boardPos, player).stream())
                .filter(KingInCheckUtils.checkKing(round, player))
                .collect(Collectors.toList());
    } // @f:y
    
    // https://en.wikipedia.org/wiki/Castling#Requirements
    private List<Move> getCastlingMoves(Round round, Square boardPos, Player player) {
        // Don't need to check for king on first rank because numMoves == 0 implies so
        if (numMoves != 0 || Utils.canBeMovedTo(round, boardPos, player)) return new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        
        for (int rookX = 0; rookX <= round.getBoard().getMaxX(); rookX += round.getBoard().getMaxX()) {
            if (canCastle(round, boardPos, player, rookX)) {
                // Use MovingMove subclass to move rook too
                int inc = (int) Math.signum(rookX - boardPos.getX());
                final int rookXFinal = rookX; // required because rookX isn't effectively final
                
                moves.add(new MovingMove(boardPos, player.perspectivize(boardPos.withX(boardPos.getX() + 2 * inc),
                        boardPos)) {
                    @Override
                    public void move(Board board) {
                        super.move(board);
                        
                        Square rookPos = boardPos.withX(rookXFinal);
                        board.set(boardPos.withX(boardPos.getX() + inc), board.get(rookPos));
                        board.set(rookPos, null);
                    }
                });
            }
        }
        
        return moves;
    }
    
    private boolean canCastle(Round round, Square boardPos, Player player, int rookX) {
        OwnedPiece pieceAtX = round.getBoard().get(player.perspectivize(boardPos.withX(rookX), boardPos));
        if (pieceAtX == null || !(pieceAtX.getPiece() instanceof Rook)) return false;
        Rook rook = (Rook) pieceAtX.getPiece();
        if (rook.getNumMoves() != 0) return false;
        
        // Ensure there are no pieces between rook & king
        int inc = (int) Math.signum(rookX - boardPos.getX());
        for (int x = boardPos.getX() + inc; x != rookX; x += inc) {
            Square between = player.perspectivize(boardPos.withX(x), boardPos);
            if (!round.getBoard().isOn(between) || round.getBoard().get(between) != null) return false;
        }
        
        // Ensure that the position the king will 'pass through' is not in check
        return !Utils.canBeMovedTo(round, player.perspectivize(boardPos.withX(boardPos.getX() + inc), boardPos),
                player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        // All 8 surrounding squares
        List<Move> moves = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Square square = player.perspectivize(new Square(boardPos.getX() + x, boardPos.getY() + y), boardPos);
                if ((square.getX() == 0 && square.getY() == 0) || !round.getBoard().isOn(square)) continue;
                
                OwnedPiece atSquare = round.getBoard().get(square);
                if (atSquare != null && atSquare.getOwner() == player) continue;
                
                moves.add(boardPos.to(square));
            }
        }
        return moves;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.piece", "king.png");
    }
    
    @Override
    public void incrementMoveCount() {
        numMoves++;
    }
    
}