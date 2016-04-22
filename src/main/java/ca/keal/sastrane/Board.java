package ca.keal.sastrane;

import ca.keal.sastrane.util.Pair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Board {
    
    @NonNull
    private final Map<Square, Pair<Piece, Player>> squaresToPieces; // TODO more dimensions?
    
    /**
     * ' ' = null piece, '_' = not on board, is error if either used in pieces.
     */
    @Builder(builderClassName = "Factory")
    public Board(@Singular @NonNull List<String> rows,
                 @Singular @NonNull Map<Character, Pair<Piece, Player>> pieces) {
        if (pieces.containsKey(' ') || pieces.containsKey('_')) {
            throw new IllegalArgumentException("Space and underscore are reserved in pieces; space is null piece "
                    + "and underscore is not-on-board.");
        }
        
        squaresToPieces = new HashMap<>();
        
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            for (int j = 0; j < row.length(); j++) {
                char piece = row.charAt(j);
                if (!pieces.containsKey(piece)) {
                    throw new IllegalArgumentException("Undeclared piece char '" + piece + "' at row " + i
                            + ", column " + j);
                }
                squaresToPieces.put(new Square(i, j), pieces.get(piece));
            }
        }
    }
    
    public Pair<Piece, Player> get(Square square) {
        return squaresToPieces.get(square);
    }
    
    public void set(Square square, Pair<Piece, Player> value) {
        squaresToPieces.put(square, value);
    }
    
    public boolean isOn(Square square) {
        return squaresToPieces.containsKey(square);
    }
    
}