package ca.keal.sastrane;

import ca.keal.sastrane.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Board {
    
    @NonNull
    private final Map<Square, Pair<Piece, Combatant>> squaresToPieces; // TODO more dimensions?
    
    public Pair<Piece, Combatant> get(Square square) {
        return squaresToPieces.get(square);
    }
    
    public void set(Square square, Pair<Piece, Combatant> value) {
        squaresToPieces.put(square, value);
    }
    
    public boolean isOn(Square square) {
        return squaresToPieces.containsKey(square);
    }
    
}