package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.RecursiveMovingPiece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.piece.JumpingPiece;
import ca.keal.sastrane.util.Resource;
import lombok.NonNull;

import java.util.List;

public class Knight extends JumpingPiece implements RecursiveMovingPiece {
    
    public Knight() {
        // "L" shape: x+2, y+1 (JumpingPiece automagically mirrors it)
        super(2, 1);
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
        return new Resource.Unmangled("ca.keal.sastrane.chess.icon", "knight.png");
    }
    
}