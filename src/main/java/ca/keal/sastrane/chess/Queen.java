package ca.keal.sastrane.chess;

import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Pair;

public class Queen extends LinePiece {
    
    public Queen() {
        super(UP | UP_RIGHT | RIGHT | DOWN_RIGHT | DOWN | DOWN_LEFT | LEFT | UP_LEFT);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Queen's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_queen");
    }
    
}