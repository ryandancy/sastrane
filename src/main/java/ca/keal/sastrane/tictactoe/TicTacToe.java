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
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameInfo;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.function.Function;

class TicTacToe implements GameInfo {
    
    @Override
    public String getResourceBundleName() {
        return "ca.keal.sastrane.tictactoe.i18n.tictactoe";
    }
    
    @Override
    public String getI18nName() {
        return "tictactoe.name";
    }
    
    @Override
    public Resource getIcon() {
        return new Resource("ca.keal.sastrane.tictactoe", "tictactoe.png");
    }
    
    @Override
    public Resource getCss() {
        return new Resource("ca.keal.sastrane.tictactoe", "tictactoe.css");
    }
    
    @Override
    public Player[] getPlayers() {
        return TicTacToePlayer.values();
    }
    
    @Override
    public Function<Double, AI> getAI() {
        return TicTacToeAI::new;
    }
    
    @Override
    public Board.Factory getBoardFactory() {
        return Board.factory()
                .row("   ")
                .row("   ")
                .row("   ");
    }
    
    @Override
    public PlacingPiece[] getPlacingPieces() {
        return new PlacingPiece[] {new Mark()};
    }
    
    @Override
    public Arbitrator getArbitrator() {
        return new TicTacToeArbitrator();
    }
    
}