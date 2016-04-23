package ca.keal.sastrane.chess;

import ca.keal.sastrane.piece.JumpingPiece;
import ca.keal.sastrane.util.Pair;

public class Knight extends JumpingPiece {
    
    public Knight() {
        // "L" shape: x+2, y+1 (JumpingPiece automagically mirrors it)
        super(2, 1);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Knight's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_knight");
    }
    
}