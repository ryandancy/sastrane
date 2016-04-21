package ca.keal.sastrane;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class Board {
    
    @NonNull
    private final Map<Square, Piece> squaresToPieces;
    
    // TODO more dimensions?
    public Board(@NonNull Map<Square, Piece> squaresToPieces) {
        this.squaresToPieces = squaresToPieces;
    }
    
    public Piece get(Square square) {
        return squaresToPieces.get(square);
    }
    
    public void set(Square square, Piece value) {
        squaresToPieces.put(square, value);
    }
    
    public boolean isOn(Square square) {
        return squaresToPieces.containsKey(square);
    }
    
}