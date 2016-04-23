package ca.keal.sastrane;

import lombok.NonNull;

/**
 * A general description of a player/team/side/whatever. If you're looking for an AI-or-user input type,
 * go to {@link Mover}.
 * <p />
 * This interface is designed to be used as an "extensible enum": each implementation of this interface
 * should be an {@code enum}. For example:
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
    
    @NonNull
    String getName();
    
    /**
     * Returns the square from this player's "perspective".
     * <p />
     * {@link Piece#getPossibleMoves} returns how the piece would move, assuming that the piece is
     * facing forwards (+y). However, for some players, this is not the case. For example, in chess, white is facing
     * forwards (+y) and black is facing towards the back of the board (-y). A black pawn will return the square
     * [x, y+1]; however, this would have it move towards its own side! To counteract this, {@code perspectivize}
     * is called on each result, and the black pawn now moves towards white's side.
     * <p />
     * Multiple static utility functions are provided as implementations of perspectivize: {@link #flipBoth},
     * {@link #flipX}, {@link #flipY}, and {@link #flipNone}.
     * 
     * @param pos The base position in question; most implementations will "flip" over this.
     * @param original The original square being looked at from another perspective.
     * @return {@code original} from this perspective.
     * @see #flipBoth(Square, Square)
     * @see #flipX(Square, Square)
     * @see #flipY(Square, Square)
     * @see #flipNone(Square, Square)
     */
    // TODO some form of full-board perspectivize
    @NonNull
    Square perspectivize(@NonNull Square pos, @NonNull Square original);
    
    @NonNull
    static Square flipNone(@SuppressWarnings("UnusedParameters") @NonNull Square pos, @NonNull Square original) {
        return original;
    }
    
    // DRY violated???
    @NonNull
    static Square flipX(@NonNull Square pos, @NonNull Square original) {
        return new Square(pos.getX() - (pos.getX() - original.getX()), original.getY());
    }
    
    @NonNull
    static Square flipY(@NonNull Square pos, @NonNull Square original) {
        return new Square(original.getX(), pos.getY() - (pos.getY() - original.getY()));
    }
    
    @NonNull
    static Square flipBoth(@NonNull Square pos, @NonNull Square original) {
        return new Square(pos.getX() - (pos.getX() - original.getX()), pos.getY() - (pos.getY() - original.getY()));
    }
    
}