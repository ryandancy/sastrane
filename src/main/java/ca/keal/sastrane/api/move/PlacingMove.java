package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@RequiredArgsConstructor
public class PlacingMove implements Move {
    
    private final OwnedPiece placement;
    private final Square pos;
    
    @Override
    public void move(Board board) {
        board.set(pos, placement);
    }
    
    @Override
    public Move perspectivize(Player player) {
        return this;
    }
    
    @Override
    public Square getEndPos() {
        return pos;
    }
    
}