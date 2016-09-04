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
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Utils;

class Reversi extends Game {
    
    Reversi() {
        super("reversi", "ca.keal.sastrane.reversi");
    }
    
    @Override
    public ReversiPlayer[] getPlayers() {
        return ReversiPlayer.values();
    }
    
    @Override
    public AI.Factory getAIFactory() {
        return Utils.instantiateFactory(AI.Factory.class, ReversiAI.class);
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
    public ReversiArbitrator getArbitrator() {
        return new ReversiArbitrator();
    }
    
    @Override
    public Disk[] getPlacingPieces() {
        return new Disk[] {new Disk()};
    }
    
    @Override
    public GridNotater getNotater() {
        return new GridNotater();
    }
    
}