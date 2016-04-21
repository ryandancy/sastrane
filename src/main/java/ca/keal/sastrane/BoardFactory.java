package ca.keal.sastrane;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@ToString
@EqualsAndHashCode
public class BoardFactory {
    
    private final Map<Square, Piece> squaresToPieces = new HashMap<>();
    
    @Builder(builderMethodName = "squareBuilder", builderClassName = "SquareBuilder")
    public BoardFactory(@Singular @NonNull Set<Square> squares, @Singular @NonNull Set<SquareRange> ranges) {
        for (Square square : squares) {
            squaresToPieces.put(square, null);
        }
    
        // Can't use addAll because SquareRange isn't a Collection
        for (SquareRange range : ranges) {
            for (Square square : range) {
                squaresToPieces.put(square, null);
            }
        }
    }
    
    /**
     * ' ' = null piece, '_' = not on board, is error if either used in pieces.
     */
    @Builder(builderMethodName = "rowBuilder", builderClassName = "RowBuilder")
    public BoardFactory(@Singular @NonNull List<String> rows, @Singular @NonNull Map<Character, Piece> pieces) {
        if (pieces.containsKey(' ') || pieces.containsKey('_')) {
            throw new IllegalArgumentException("Space and underscore are reserved in pieces; space is null piece " 
                    + "and underscore is not-on-board.");
        }
        
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
    
    public Board create() {
        return new Board(squaresToPieces);
    }
    
    @Value
    public static final class SquareRange implements Iterable<Square> {
        
        private final Square a;
        private final Square b;
        
        public SquareRange(@NonNull Square a, @NonNull Square b) {
            this.a = new Square(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
            this.b = new Square(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()));
        }
        
        /**
         * Iterates over x, then y. Ex: {@code SquareRange((1, 2), (5, 5)).iterator()} ->
         * <pre>{@code
         *     (1, 2), (2, 2), (3, 2), (4, 2), (5, 2),
         *     (1, 3), (2, 3), (3, 3), (4, 3), (5, 3),
         *     (1, 4), (2, 4), (3, 4), (4, 4), (5, 4),
         *     (1, 5), (2, 5), (3, 5), (4, 5), (5, 5)
         * }</pre>.
         */
        @Override
        public Iterator<Square> iterator() {
            return new Iterator<Square>() {
                
                private int x = a.getX();
                private int y = a.getY();
                
                @Override
                public boolean hasNext() {
                    return x <= b.getX() && y <= b.getY();
                }
                
                @NonNull
                @Override
                public Square next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException("ca.keal.sastrane.BoardFactory.SquareRange.iterator");
                    }
                    if (x >= b.getX()) {
                        x = 0;
                        y++;
                    } else {
                        x++;
                    }
                    return new Square(x, y);
                }
            };
        }
        
    }
    
}