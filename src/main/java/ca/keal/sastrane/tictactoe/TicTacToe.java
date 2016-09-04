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
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import com.google.inject.assistedinject.FactoryProvider;

class TicTacToe extends Game {
    
    TicTacToe() {
        super("tictactoe", "ca.keal.sastrane.tictactoe");
    }
    
    @Override
    public TicTacToePlayer[] getPlayers() {
        return TicTacToePlayer.values();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public AI.Factory getAIFactory() {
        return FactoryProvider.newFactory(AI.Factory.class, TicTacToeAI.class).get();
    }
    
    @Override
    public Board.Factory getBoardFactory() {
        return Board.factory()
                .row("   ")
                .row("   ")
                .row("   ");
    }
    
    @Override
    public Mark[] getPlacingPieces() {
        return new Mark[] {new Mark()};
    }
    
    @Override
    public TicTacToeArbitrator getArbitrator() {
        return new TicTacToeArbitrator();
    }
    
}