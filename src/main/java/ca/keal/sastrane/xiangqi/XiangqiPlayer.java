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

package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

@RequiredArgsConstructor
public enum XiangqiPlayer implements Player {
    
    RED("red", "xiangqi.player.red", new Resource("ca.keal.sastrane.xiangqi.piece", "general_red.png"),
            Player::flipY, Player::flipAroundCentre, ImmutableList.of(
                new Square(3, 9), new Square(4, 9), new Square(5, 9),
                new Square(3, 8), new Square(4, 8), new Square(5, 8),
                new Square(3, 7), new Square(4, 7), new Square(5, 7))),
    BLACK("black", "xiangqi.player.black", new Resource("ca.keal.sastrane.xiangqi.piece", "general_black.png"),
            Player::flipNone, Player::flipNone, ImmutableList.of(
                new Square(3, 2), new Square(4, 2), new Square(5, 2),
                new Square(3, 1), new Square(4, 1), new Square(5, 1),
                new Square(3, 0), new Square(4, 0), new Square(5, 0)));
    
    @Getter private final String name;
    @Getter private final String i18nName;
    @Getter private final Resource icon;
    private final BinaryOperator<Square> perspectivizer;
    private final UnaryOperator<Square> notationPerspectivizer;
    @Getter private final List<Square> palace;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return perspectivizer.apply(pos, original);
    }
    
    public Square perspectivizeNotation(Square pos) {
        return notationPerspectivizer.apply(pos);
    }
    
}