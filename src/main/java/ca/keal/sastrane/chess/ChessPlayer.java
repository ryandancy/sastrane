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

package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BinaryOperator;

@RequiredArgsConstructor
enum ChessPlayer implements Player {
    
    // y = 0 is at top
    WHITE("white", "chess.player.white", new Resource("ca.keal.sastrane.chess.icon", "white.png"), Player::flipY),
    BLACK("black", "chess.player.black", new Resource("ca.keal.sastrane.chess.icon", "black.png"), Player::flipNone);
    
    @Getter private final String name;
    @Getter private final String i18nName;
    @Getter private final Resource icon;
    private final BinaryOperator<Square> perspectivizer;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return perspectivizer.apply(pos, original);
    }
    
}