package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@RequiredArgsConstructor
public class PlacingMove implements Move {
    
    private final Pair<Piece, Player> placement;
    private final Square pos;
    
    @Override
    public void move(@NonNull Board board) {
        board.set(pos, placement);
    }
    
    @Override
    public Move perspectivize(@NonNull Player player) {
        return this;
    }
    
    @Override
    @NonNull
    public Square getEndPos() {
        return pos;
    }
    
}