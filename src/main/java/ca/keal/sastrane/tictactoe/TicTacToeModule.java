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
import ca.keal.sastrane.api.Player;
import com.google.inject.TypeLiteral;

import java.util.function.Function;

public class TicTacToeModule extends AbstractGameModule {
    
    public TicTacToeModule() {
        super(TicTacToeGame.class, TicTacToe.class);
    }
    
    @Override
    public void configure() {
        super.configure();
        
        bindToInstance(Game.Name.class, new Game.Name("tictactoe"));
        bindToInstance(Game.Package.class, new Game.Package("ca.keal.sastrane.tictactoe"));
        bindToInstance(Player[].class, TicTacToePlayer.values());
        bindToInstance(new TypeLiteral<Function<Double, AI>>() {}, TicTacToeAI::new);
        bindToInstance(Board.Factory.class, Board.factory()
                .row("   ")
                .row("   ")
                .row("   "));
        bindTo(Arbitrator.class, TicTacToeArbitrator.class);
    }
    
}