/*
 * Copyright (C) 2016 Ryan Dancy
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Board implements Iterable<Square> {
    
    // Use JavaFX ObservableMap so that listeners can be added
    private final ObservableMap<Square, OwnedPiece> squaresToPieces; // TODO more dimensions?
    
    @Getter(lazy = true)
    private final int maxX = getMaxDimen(Square::getX);
    
    @Getter(lazy = true)
    private final int maxY = getMaxDimen(Square::getY);
    
    /**
     * ' ' = null piece, '_' = not on board, is error if either used in pieces.
     */
    @Builder(builderClassName = "Factory", builderMethodName = "factory")
    public Board(EventBus bus, @Singular List<String> rows, @Singular Map<Character, OwnedPieceFactory> pieces) {
        if (pieces.containsKey(' ') || pieces.containsKey('_')) {
            throw new IllegalArgumentException("Space and underscore are reserved in pieces; space is null piece "
                    + "and underscore is not-on-board.");
        }
        if (rows.size() != 0 && rows.stream().map(String::length).distinct().count() != 1) {
            throw new IllegalArgumentException("Inconsistent length of row; use '_' to skip a square.");
        }
        
        Map<Square, OwnedPiece> squaresToPieces = new HashMap<>();
        Set<Character> charsUsed = new HashSet<>();
        
        for (int y = 0; y < rows.size(); y++) {
            String row = rows.get(y);
            for (int x = 0; x < row.length(); x++) {
                char piece = row.charAt(x);
                if (piece == '_') continue;
                if (piece != ' ') {
                    if (!pieces.containsKey(piece)) {
                        throw new IllegalArgumentException("Undeclared piece char '" + piece + "' at row " + y
                                + ", column " + x);
                    }
                    charsUsed.add(piece);
                }
                squaresToPieces.put(new Square(x, y), piece == ' ' ? null
                        : new OwnedPiece(pieces.get(piece).getPiece(bus), pieces.get(piece).getPlayer()));
            }
        }
        
        if (!charsUsed.equals(pieces.keySet())) {
            throw new IllegalArgumentException("Too many pieces!");
        }
        
        this.squaresToPieces = FXCollections.observableMap(squaresToPieces);
    }
    
    public Board(Board board) {
        this(FXCollections.observableMap(new HashMap<>(board.squaresToPieces)));
    }
    
    @Nullable
    public OwnedPiece get(Square square) {
        if (!isOn(square)) {
            throw new NoSuchElementException(square + " is not on the board");
        }
        return squaresToPieces.get(square);
    }
    
    public void set(Square square, @Nullable OwnedPiece value) {
        if (!isOn(square)) {
            throw new IllegalArgumentException(square + " is not on the board");
        }
        squaresToPieces.put(square, value);
    }
    
    public void addListener(MapChangeListener<Square, OwnedPiece> listener) {
        squaresToPieces.addListener(listener);
    }
    
    public boolean isOn(Square square) {
        return squaresToPieces.containsKey(square);
    }
    
    /**
     * Pass Square::getX for maxX, or Square::getY for maxY
     */
    private int getMaxDimen(Function<Square, Integer> map) {
        if (squaresToPieces.size() == 0) {
            return 0;
        } else {
            return Collections.max(squaresToPieces.keySet().stream().map(map).collect(Collectors.toList()));
        }
    }
    
    @Override
    public Iterator<Square> iterator() {
        return squaresToPieces.keySet().iterator();
    }
    
}