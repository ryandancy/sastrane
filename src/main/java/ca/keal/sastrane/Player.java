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
 *         PLAYERA("a"), PLAYERB("b");
 *         
 *         private String name;
 *         
 *         FooPlayer(String name) {
 *             this.name = name;
 *         }
 *         
 *        {@literal @}Override
 *         public String getName() {
 *             return name;
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
    
}