package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.piece.JumpingPiece;
import ca.keal.sastrane.util.Pair;
import lombok.NonNull;

import java.util.List;

public class Knight extends JumpingPiece {
    
    public Knight() {
        // "L" shape: x+2, y+1 (JumpingPiece automagically mirrors it)
        super(2, 1);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        return KingInCheckUtils.getPossibleMoves(this, round, boardPos, player);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        return Pair.of("ca.keal.sastrane.chess.icon", "knight");
    }
    
}