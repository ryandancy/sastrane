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

package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MovingMove implements Move {
    
    @Getter private final Square from;
    @Getter private final Square to;
    
    @Override
    public void move(Board board) {
        if (!board.isOn(from) || !board.isOn(to)) {
            throw new IllegalArgumentException("MovingMove.move: from & to must be on board");
        }
        
        board.set(to, board.get(from));
        board.set(from, null);
    }
    
    @Override
    public Move perspectivize(Player player) {
        return from.to(player.perspectivize(from, to));
    }
    
    @Override
    public Square getEndPos() {
        return to;
    }
    
}