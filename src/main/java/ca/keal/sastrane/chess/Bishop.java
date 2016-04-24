package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Pair;
import lombok.NonNull;

import java.util.List;

public class Bishop extends LinePiece {
    
    public Bishop() {
        super(UP_LEFT | UP_RIGHT | DOWN_LEFT | DOWN_RIGHT);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        return KingInCheckUtils.getPossibleMoves(this, round, boardPos, player);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Bishop's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_bishop");
    }
    
}