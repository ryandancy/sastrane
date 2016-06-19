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

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameInfo;
import ca.keal.sastrane.api.Notatable;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Resource;

import java.util.function.Function;

class Chess implements GameInfo, Notatable {
    
    @Override
    public String getResourceBundleName() {
        return "ca.keal.sastrane.chess.i18n.chess";
    }
    
    @Override
    public String getI18nName() {
        return "chess.name";
    }
    
    @Override
    public Resource getIcon() {
        return new Resource("ca.keal.sastrane.chess.icon", "chess.png");
    }
    
    @Override
    public Resource getCss() {
        return new Resource("ca.keal.sastrane.chess", "chess.css");
    }
    
    @Override
    public Player[] getPlayers() {
        return ChessPlayer.values();
    }
    
    @Override
    public Function<Double, AI> getAI() {
        return ChessAI::new;
    }
    
    @Override
    public Board.Factory getBoardFactory() {
        return Board.factory()
                .row("RNBQKBNR")
                .row("PPPPPPPP")
                .row("        ")
                .row("        ")
                .row("        ")
                .row("        ")
                .row("pppppppp")
                .row("rnbqkbnr")
                .piece('R', new OwnedPieceFactory(Rook::new, ChessPlayer.BLACK))
                .piece('N', new OwnedPieceFactory(Knight::new, ChessPlayer.BLACK))
                .piece('B', new OwnedPieceFactory(Bishop::new, ChessPlayer.BLACK))
                .piece('Q', new OwnedPieceFactory(Queen::new, ChessPlayer.BLACK))
                .piece('K', new OwnedPieceFactory(King::new, ChessPlayer.BLACK))
                .piece('P', new OwnedPieceFactory(Pawn::new, ChessPlayer.BLACK))
                .piece('r', new OwnedPieceFactory(Rook::new, ChessPlayer.WHITE))
                .piece('n', new OwnedPieceFactory(Knight::new, ChessPlayer.WHITE))
                .piece('b', new OwnedPieceFactory(Bishop::new, ChessPlayer.WHITE))
                .piece('q', new OwnedPieceFactory(Queen::new, ChessPlayer.WHITE))
                .piece('k', new OwnedPieceFactory(King::new, ChessPlayer.WHITE))
                .piece('p', new OwnedPieceFactory(Pawn::new, ChessPlayer.WHITE));
    }
    
    @Override
    public Arbitrator getArbitrator() {
        return ChessArbitrator.INSTANCE;
    }
    
    @Override
    public Notater getNotater() {
        return new LongAlgebraicNotater();
    }
    
}