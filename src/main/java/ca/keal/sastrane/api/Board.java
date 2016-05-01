package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Pair;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Board implements Iterable<Square> {
    
    // Use JavaFX ObservableMap so that listeners can be added
    @NonNull
    private final ObservableMap<Square, Pair<Piece, Player>> squaresToPieces; // TODO more dimensions?
    
    @Getter(lazy = true)
    private final int maxX = getMaxDimen(Square::getX);
    
    @Getter(lazy = true)
    private final int maxY = getMaxDimen(Square::getY);
    
    /**
     * ' ' = null piece, '_' = not on board, is error if either used in pieces.
     */
    @Builder(builderClassName = "Factory", builderMethodName = "factory")
    public Board(@Singular @NonNull List<String> rows,
                 @Singular @NonNull Map<Character, Pair<Supplier<Piece>, Player>> pieces) {
        if (pieces.containsKey(' ') || pieces.containsKey('_')) {
            throw new IllegalArgumentException("Space and underscore are reserved in pieces; space is null piece "
                    + "and underscore is not-on-board.");
        }
        
        Map<Square, Pair<Piece, Player>> squaresToPieces = new HashMap<>();
        
        for (int y = 0; y < rows.size(); y++) {
            String row = rows.get(y);
            for (int x = 0; x < row.length(); x++) {
                char piece = row.charAt(x);
                if (piece == '_') continue;
                if (piece != ' ' && !pieces.containsKey(piece)) {
                    throw new IllegalArgumentException("Undeclared piece char '" + piece + "' at row " + y
                            + ", column " + x);
                }
                squaresToPieces.put(new Square(x, y), piece == ' ' ? null
                        : pieces.get(piece).withLeft(pieces.get(piece).getLeft().get()));
            }
        }
        
        this.squaresToPieces = FXCollections.observableMap(squaresToPieces);
    }
    
    public Board(@NonNull Board board) {
        this(FXCollections.observableMap(new HashMap<>(board.squaresToPieces)));
    }
    
    public Pair<Piece, Player> get(Square square) {
        return squaresToPieces.get(square);
    }
    
    public void set(Square square, Pair<Piece, Player> value) {
        squaresToPieces.put(square, value);
    }
    
    public void addListener(MapChangeListener<Square, Pair<Piece, Player>> listener) {
        squaresToPieces.addListener(listener);
    }
    
    public boolean isOn(Square square) {
        return squaresToPieces.containsKey(square);
    }
    
    /**
     * Pass Square::getX for maxX, or Square::getY for maxY
     */
    private int getMaxDimen(Function<Square, Integer> map) {
        return Collections.max(squaresToPieces.keySet().stream().map(map).collect(Collectors.toList()));
    }
    
    @Override
    public Iterator<Square> iterator() {
        return squaresToPieces.keySet().iterator();
    }
    
}