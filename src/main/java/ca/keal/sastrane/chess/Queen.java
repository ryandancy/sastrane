package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.util.Resource;
import lombok.NonNull;

import java.util.List;

public class Queen extends LinePiece implements RecursiveMovingPiece {
    
    public Queen() {
        super(UP | UP_RIGHT | RIGHT | DOWN_RIGHT | DOWN | DOWN_LEFT | LEFT | UP_LEFT);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        return KingInCheckUtils.getPossibleMoves(super::getPossibleMoves, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(@NonNull Round round, @NonNull Square boardPos,
                                                   @NonNull Player player) {
        return super.getPossibleMoves(round, boardPos, player);
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.piece", "queen.png");
    }
    
}