package ca.keal.sastrane.chess;

import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Pair;

public class Bishop extends LinePiece {
    
    public Bishop() {
        super(UP_LEFT | UP_RIGHT | DOWN_LEFT | DOWN_RIGHT);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Bishop's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_bishop");
    }
    
}