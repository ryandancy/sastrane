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

package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.AbstractGameModule;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameAttrib;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.PlacingPiece;

public class TicTacToeModule extends AbstractGameModule {
    
    public TicTacToeModule() {
        super("ca.keal.sastrane.tictactoe");
    }
    
    @Override
    public void configure() {
        bindToInstance(GameAttrib.NAME, String.class, "tictactoe");
        bindToInstance(GameAttrib.PACKAGE, String.class, "ca.keal.sastrane.tictactoe");
        bindToInstance(GameAttrib.PLAYERS, Player[].class, TicTacToePlayer.values());
        installFactory(GameAttrib.AI, AI.Factory.class, TicTacToeAI.class);
        bindToInstance(GameAttrib.BOARD_FACTORY, Board.Factory.class, Board.factory()
                .row("   ")
                .row("   ")
                .row("   "));
        bindToInstance(GameAttrib.PLACING_PIECES, PlacingPiece[].class, new PlacingPiece[] {new Mark()});
        bindTo(GameAttrib.ARBITRATOR, Arbitrator.class, TicTacToeArbitrator.class);
    
        super.configure();
    }
    
}