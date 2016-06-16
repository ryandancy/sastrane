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

package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.xiangqi.Xiangqi;

/**
 * A general description of a player/team/side/whatever. If you're looking for an AI-or-user input type, go to {@link
 * Mover}.
 * <p>
 * This interface is designed to be used as an "extensible enum": each implementation of this interface should be an
 * {@code enum}. For example:
 * <pre><code>
 *     public enum FooPlayer implements Player {
 *
 *         PLAYERA("a", Player::flipNone), PLAYERB("b", Player::flipY);
 *
 *         private String name;
 *         private BinaryOperator&lt;Square&gt; perspectivizer;
 *
 *         FooPlayer(String name, BinaryOperator&lt;Square&gt; perspectivizer) {
 *             this.name = name;
 *             this.perspectivizer = perspectivizer;
 *         }
 *
 *        {@literal @}Override
 *         public String getName() {
 *             return name;
 *         }
 *
 *        {@literal @}Override
 *         public Square perspectivize(Square pos, Square original) {
 *             return perspectivizer.apply(pos, original);
 *         }
 *
 *     }
 * </code></pre>
 *
 * @see Mover
 */
public interface Player {
    
    String getI18nName();
    
    String getName();
    
    Resource getIcon();
    
    /**
     * Returns the square from this player's "perspective".
     * <p>
     * {@link MovingPiece#getPossibleMoves} returns how the piece would move, assuming that the piece is facing forwards
     * (+y). However, for some players, this is not the case. For example, in chess, white is facing forwards (+y) and
     * black is facing towards the back of the board (-y). A black pawn will return the square [x, y+1]; however, this
     * would have it move towards its own side! To counteract this, {@code perspectivize} is called on each result, and
     * the black pawn now moves towards white's side.
     * <p>
     * Multiple static utility functions are provided as implementations of perspectivize: {@link #flipBoth}, {@link
     * #flipX}, {@link #flipY}, and {@link #flipNone}.
     *
     * @param pos The base position in question; most implementations will "flip" over this.
     * @param original The original square being looked at from another perspective.
     * @return {@code original} from this perspective.
     * @see #flipBoth(Square, Square)
     * @see #flipX(Square, Square)
     * @see #flipY(Square, Square)
     * @see #flipNone(Square, Square)
     */
    Square perspectivize(Square pos, Square original);
    
    static Square flipNone(Square pos, @SuppressWarnings("UnusedParameters") Square original) {
        return flipNone(pos);
    }
    
    static Square flipNone(Square pos) {
        return pos;
    }
    
    // DRY violated???
    static Square flipX(Square pos, Square original) {
        return new Square(2 * original.getX() - pos.getX(), pos.getY());
    }
    
    static Square flipY(Square pos, Square original) {
        return new Square(pos.getX(), 2 * original.getY() - pos.getY());
    }
    
    static Square flipBoth(Square pos, Square original) {
        return new Square(2 * original.getX() - pos.getX(), 2 * original.getY() - pos.getY());
    }
    
    static Square flipAroundCentre(Square pos, @SuppressWarnings("UnusedParameters") Square original) {
        return flipAroundCentre(pos);
    }
    
    static Square flipAroundCentre(Square pos) {
        return new Square(Xiangqi.MAXX - pos.getX(), Xiangqi.MAXY - pos.getY());
    }
    
}