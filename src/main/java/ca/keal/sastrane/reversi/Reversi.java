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
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Notatable;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.function.Function;

class Reversi implements Game, Notatable {
    
    @Override
    public String getResourceBundleName() {
        return "ca.keal.sastrane.reversi.i18n.reversi";
    }
    
    @Override
    public String getName() {
        return "reversi";
    }
    
    @Override
    public Resource getIcon() {
        return new Resource("ca.keal.sastrane.reversi", "reversi.png");
    }
    
    @Override
    public Resource getCss() {
        return new Resource("ca.keal.sastrane.reversi", "reversi.css");
    }
    
    @Override
    public Player[] getPlayers() {
        return ReversiPlayer.values();
    }
    
    @Override
    public Function<Double, AI> getAI() {
        return ReversiAI::new;
    }
    
    @Override
    public Board.Factory getBoardFactory() {
        return Board.factory()
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .row("   WB   ")
                        .row("   BW   ")
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .piece('B', new OwnedPieceFactory(Disk::new, ReversiPlayer.BLACK))
                        .piece('W', new OwnedPieceFactory(Disk::new, ReversiPlayer.WHITE));
    }
    
    @Override
    public PlacingPiece[] getPlacingPieces() {
        return new PlacingPiece[] {new Disk()};
    }
    
    @Override
    public Arbitrator getArbitrator() {
        return new ReversiArbitrator();
    }
    
    @Override
    public Notater getNotater() {
        return new GridNotater();
    }
    
}