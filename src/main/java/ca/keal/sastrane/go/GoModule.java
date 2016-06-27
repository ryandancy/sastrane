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

package ca.keal.sastrane.go;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.AbstractGameModule;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.BoardDecor;
import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.PlacingPiece;
import com.google.inject.name.Names;

/**
 * Implements the Chinese rules of go.
 */
public class GoModule extends AbstractGameModule {
    
    public GoModule() {
        super("ca.keal.sastrane.go");
    }
    
    @Override
    public void configure() {
        bindToInstance(GameAttr.NAME, String.class, "go");
        bindToInstance(GameAttr.PACKAGE, String.class, "ca.keal.sastrane.go");
        bindToInstance(GameAttr.PLAYERS, Player[].class, GoPlayer.values());
        installFactory(GameAttr.AI, AI.Factory.class, GoAI.class);
        bindToInstance(GameAttr.BOARD_FACTORY, Board.Factory.class, Board.factory()
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ") // A 19x19 empty square
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   "));
        bindToInstance(GameAttr.PLACING_PIECES, PlacingPiece[].class, new PlacingPiece[] {new Stone()});
        bindTo(GameAttr.ARBITRATOR, Arbitrator.class, GoArbitrator.class);
        bindTo(GameAttr.BOARD_DECOR, BoardDecor.class, StarPointsDecor.class);
        bindToInstance(GameAttr.ALLOW_PASSING, Boolean.class, true);
        
        // NOTE: There does not appear to be any standard human-readable computer go notation system, which is why this
        // implementation does not include one. The kifu system only really works when written by hand.
        
        // Chinese rules: komi is 7.5 points (http://senseis.xmp.net/?Komi)
        bindConstant().annotatedWith(Names.named("komi")).to(7.5);
        
        super.configure();
    }
    
}