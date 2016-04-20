package ca.keal.sastrane;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class Board {
    
    @NonNull
    private final Map<Square, Piece> squaresToPieces;
    
    // TODO more dimensions?
    @Builder
    private Board(@Singular Set<Square> squares, @Singular List<SquareRange> ranges) {
        squaresToPieces = new HashMap<>();
        
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
    
    public Piece get(Square square) {
        return squaresToPieces.get(square);
    }
    
    public void set(Square square, Piece value) {
        squaresToPieces.put(square, value);
    }
    
    public boolean isOnBoard(Square square) {
        return squaresToPieces.containsKey(square);
    }
    
    @Value
    public final class SquareRange implements Iterable<Square> {
        
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
                        throw new NoSuchElementException("ca.keal.sastrane.Board.SquareRange.iterator");
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