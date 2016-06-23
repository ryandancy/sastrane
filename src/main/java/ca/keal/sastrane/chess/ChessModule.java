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
import ca.keal.sastrane.api.AbstractGameModule;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;

public class ChessModule extends AbstractGameModule {
    
    public ChessModule() {
        super("ca.keal.sastrane.chess");
    }
    
    @Override
    public void configure() {
        bindToInstance(GameAttr.NAME, String.class, "chess");
        bindToInstance(GameAttr.PACKAGE, String.class, "ca.keal.sastrane.chess");
        bindToInstance(GameAttr.PLAYERS, Player[].class, ChessPlayer.values());
        installFactory(GameAttr.AI, AI.Factory.class, ChessAI.class);
        bindToInstance(GameAttr.BOARD_FACTORY, Board.Factory.class, Board.factory()
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
                .piece('p', new OwnedPieceFactory(Pawn::new, ChessPlayer.WHITE)));
        bindTo(GameAttr.ARBITRATOR, Arbitrator.class, ChessArbitrator.class);
        bindTo(GameAttr.NOTATER, Notater.class, LongAlgebraicNotater.class);
    
        super.configure();
    }
    
}