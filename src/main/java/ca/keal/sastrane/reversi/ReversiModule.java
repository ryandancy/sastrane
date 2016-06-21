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

package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.AbstractGameModule;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import com.google.inject.TypeLiteral;

import java.util.function.Function;

public class ReversiModule extends AbstractGameModule {
    
    public ReversiModule() {
        super(ReversiGame.class);
    }
    
    @Override
    public void configure() {
        super.configure();
        
        bind(Game.Name.class)
                .annotatedWith(Reversi.class)
                .toInstance(new Game.Name("reversi"));
        bind(Game.Package.class)
                .annotatedWith(Reversi.class)
                .toInstance(new Game.Package("ca.keal.sastrane.reversi"));
        bind(Player[].class)
                .annotatedWith(Reversi.class)
                .toInstance(ReversiPlayer.values());
        bind(new TypeLiteral<Function<Double, AI>>() {})
                .annotatedWith(Reversi.class)
                .toInstance(ReversiAI::new);
        bind(Board.Factory.class)
                .annotatedWith(Reversi.class)
                .toInstance(Board.factory()
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .row("   WB   ")
                        .row("   BW   ")
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .piece('B', new OwnedPieceFactory(Disk::new, ReversiPlayer.BLACK))
                        .piece('W', new OwnedPieceFactory(Disk::new, ReversiPlayer.WHITE)));
        bind(Arbitrator.class)
                .annotatedWith(Reversi.class)
                .to(ReversiArbitrator.class);
        bind(Notater.class)
                .annotatedWith(Reversi.class)
                .to(GridNotater.class);
    }
    
}