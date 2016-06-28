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

package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Notates xiangqi games using the notation popularized by the World Xiangqi Federation (WXF), known as <a
 * href="https://en.wikipedia.org/wiki/Xiangqi#System_2">WXF notation</a>.
 */
@EqualsAndHashCode
@ToString
class WXFNotater implements Notater {
    
    private static final Map<Class<? extends Piece>, Character> PIECE_CHARS = new HashMap<>();
    
    static {
        PIECE_CHARS.put(Advisor.class, 'A');
        PIECE_CHARS.put(Cannon.class, 'C');
        PIECE_CHARS.put(Chariot.class, 'R'); // for Rook
        PIECE_CHARS.put(Elephant.class, 'E');
        PIECE_CHARS.put(General.class, 'G');
        PIECE_CHARS.put(Horse.class, 'H');
        PIECE_CHARS.put(Soldier.class, 'S');
    }
    
    @Override
    public String notate(List<StateChange> moves) {
        val res = new StringBuilder();
        
        int moveNum = 1;
        boolean startNew = true;
        boolean isFirst = true;
        for (StateChange change : moves) {
            if (startNew) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    res.append(System.lineSeparator());
                }
                res.append(moveNum++);
                res.append('.');
            }
            res.append(' ');
            
            OwnedPiece piece = change.getMovedPiece();
            int piecesInTandem = 0;
            int posInTandem = -1;
            for (int y = 0; y <= change.getBefore().getMaxY(); y++) {
                // get square in tandem with piece
                Square square = change.getMovingMove().getFrom().withY(y);
                if (!change.getBefore().isOn(square)) continue;
                
                OwnedPiece tandemSquare = change.getBefore().get(square);
                if (piece.equals(tandemSquare)) {
                    piecesInTandem++;
                    if (y == change.getMovingMove().getFrom().getY()) {
                        posInTandem = piecesInTandem;
                    }
                }
            }
            
            // find if tandem pieces are in 2 files
            Multimap<Integer, OwnedPiece> filesToPieces = ArrayListMultimap.create();
            for (int x = 0; x <= change.getBefore().getMaxX(); x++) {
                for (int y = 0; y <= change.getBefore().getMaxY(); y++) {
                    Square square = new Square(x, y);
                    if (!change.getBefore().isOn(square)) continue;
                    
                    filesToPieces.put(x, change.getBefore().get(square));
                }
            }
            // TODO reduce all this streaming, it's way overkill
            Multimap<Integer, OwnedPiece> filesToLikePieces = ImmutableMultimap.copyOf(filesToPieces.entries().stream()
                    .filter(e -> piece.equals(e.getValue()))
                    .collect(Collectors.toList()));
            Map<Integer, Integer> filesToTandemPieces = ImmutableMap.copyOf(filesToLikePieces.keySet().stream()
                    .map(x -> Maps.immutableEntry(x, filesToLikePieces.get(x).size()))
                    .filter(e -> e.getValue() > 1) // we only want files with >=2 tandem pieces
                    .collect(Collectors.toList()));
            
            if (filesToTandemPieces.size() > 1) {
                res.append(change.getMovingMove().getFrom().getX());
            } else if (piecesInTandem >= 4 && (posInTandem == 0 || posInTandem == piecesInTandem)) {
                res.append(posInTandem == 0 ? '-' : '+');
            } else {
                res.append(PIECE_CHARS.get(piece.getPiece().getClass()));
            }
            
            XiangqiPlayer owner = (XiangqiPlayer) piece.getOwner();
            Square fromSquare = owner.perspectivizeNotation(change.getMovingMove().getFrom());
            Square toSquare = owner.perspectivizeNotation(change.getMovingMove().getTo());
            
            if (piecesInTandem >= 2 && (piecesInTandem % 2 == 0 || posInTandem * 2 - 1 != piecesInTandem)) {
                res.append(posInTandem <= piecesInTandem / 2 ? '-' : '+');
            } else {
                // former file, perspectivized
                res.append(fromSquare.getX());
            }
            
            char operator;
            switch (Integer.compare(fromSquare.getY(), toSquare.getY())) {
                case -1: {
                    operator = '-';
                    break;
                }
                case 0: {
                    operator = '.';
                    break;
                }
                case 1: {
                    operator = '+';
                    break;
                }
                default: {
                    // wut? This happens if Integer.compare returns something other than -1, 0, or 1...
                    throw new IllegalStateException("This should never happen... it's dark... and I'm scared... " 
                            + "(Integer.compare returned something other than -1, 0, or 1)");
                }
            }
            res.append(operator);
            
            if (fromSquare.getX() == toSquare.getX()) {
                // purely vertical movement: num ranks traversed
                res.append(Math.abs(toSquare.getY() - fromSquare.getY()));
            } else {
                // new file
                res.append(toSquare.getX());
            }
            
            startNew = !startNew;
        }
        
        return res.toString();
    }
    
}