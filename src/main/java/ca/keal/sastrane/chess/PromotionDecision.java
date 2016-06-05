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

package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum PromotionDecision implements Decision {
    
    // TODO find a way to refer to piece classes' icons?
    QUEEN(new Resource("ca.keal.sastrane.chess.piece", "queen_white.png"), "chess.promotion.queen", Queen::new),
    ROOK(new Resource("ca.keal.sastrane.chess.piece", "rook_white.png"), "chess.promotion.rook", Rook::new),
    BISHOP(new Resource("ca.keal.sastrane.chess.piece", "bishop_white.png"), "chess.promotion.bishop", Bishop::new),
    KNIGHT(new Resource("ca.keal.sastrane.chess.piece", "knight_white.png"), "chess.promotion.knight", Knight::new);
    
    @Getter private final Resource icon;
    @Getter private final String i18nName;
    private final Supplier<Piece> pieceSupplier;
    
    @Override
    public void onChoose(Round round) {
        Square lastMovePos = round.getMoves().peekLast().getEndPos();
        // If there's nothing at lastMovePos, we'll get an NPE - but that should be impossible
        //noinspection ConstantConditions
        round.getBoard().set(lastMovePos, new OwnedPiece(pieceSupplier.get(),
                round.getBoard().get(lastMovePos).getOwner()));
    }
    
}