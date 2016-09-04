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
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.PlacingPiece;
import com.google.inject.multibindings.Multibinder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class TicTacToeModule extends AbstractGameModule {
    
    public TicTacToeModule() {
        super("ca.keal.sastrane.tictactoe");
    }
    
    @Override
    public void configure() {
        Multibinder.newSetBinder(binder(), Game.class).addBinding().to(TicTacToe.class);
        
        // Old
        bindToInstance(GameAttr.NAME, String.class, "tictactoe");
        bindToInstance(GameAttr.PACKAGE, String.class, "ca.keal.sastrane.tictactoe");
        bindToInstance(GameAttr.PLAYERS, Player[].class, TicTacToePlayer.values());
        installFactory(GameAttr.AI, AI.Factory.class, TicTacToeAI.class);
        bindToInstance(GameAttr.BOARD_FACTORY, Board.Factory.class, Board.factory()
                .row("   ")
                .row("   ")
                .row("   "));
        bindToInstance(GameAttr.PLACING_PIECES, PlacingPiece[].class, new PlacingPiece[] {new Mark()});
        bindTo(GameAttr.ARBITRATOR, Arbitrator.class, TicTacToeArbitrator.class);
    
        super.configure();
    }
    
}