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
        super(TicTacToeGame.class);
    }
    
    @Override
    public void configure() {
        super.configure();
        
        bind(Game.Name.class)
                .annotatedWith(TicTacToe.class)
                .toInstance(new Game.Name("tictactoe"));
        bind(Game.Package.class)
                .annotatedWith(TicTacToe.class)
                .toInstance(new Game.Package("ca.keal.sastrane.tictactoe"));
        bind(Player[].class)
                .annotatedWith(TicTacToe.class)
                .toInstance(TicTacToePlayer.values());
        bind(new TypeLiteral<Function<Double, AI>>() {})
                .annotatedWith(TicTacToe.class)
                .toInstance(TicTacToeAI::new);
        bind(Board.Factory.class)
                .annotatedWith(TicTacToe.class)
                .toInstance(Board.factory()
                        .row("   ")
                        .row("   ")
                        .row("   "));
        bind(Arbitrator.class)
                .annotatedWith(TicTacToe.class)
                .to(TicTacToeArbitrator.class);
    }
    
}