package ca.keal.sastrane.chess;

import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Pair;

public class Rook extends LinePiece {
    
    public Rook() {
        super(UP | LEFT | DOWN | RIGHT);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Rook's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_rook");
    }
    
}