package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.util.Resource;

public interface Piece {
    
    /**
     * <strong>Excludes file extension!!!</strong> File extension is assumed to be .png; it + the player's name will be
     * mangled together to form the final filename. For example a piece that returns {@code ("com.foo", "bar")} in this
     * method, and whose player's {@link Player#getName()} method returns {@code "baz"}, will have a final image
     * filename of {@code "com/foo/bar_baz.png"}.
     *
     * @return (packageName, filename) of the piece's icon, with the file name <strong>without the extension!</strong>
     */
    Resource.Unmangled getImage();
    
}