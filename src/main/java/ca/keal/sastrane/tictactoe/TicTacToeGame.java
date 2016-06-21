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
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.PlacingPiece;
import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class TicTacToeGame implements Game {
    
    private final Game.Name name;
    private final Game.Package packageName;
    private final Player[] players;
    private final Function<Double, AI> aI;
    private final Board.Factory boardFactory;
    private final PlacingPiece[] placingPieces;
    private final Arbitrator arbitrator;
    @Getter(AccessLevel.NONE) private final Void __; // so as not to cause constructor conflicts with Lombok
    
    @Inject
    public TicTacToeGame(@TicTacToe Game.Name name, @TicTacToe Game.Package packageName, @TicTacToe Player[] players,
                         @TicTacToe Function<Double, AI> ai, @TicTacToe Board.Factory boardFactory,
                         @TicTacToe PlacingPiece[] placingPieces, @TicTacToe Arbitrator arbitrator) {
        this(name, packageName, players, ai, boardFactory, placingPieces, arbitrator, null);
    }
    
}