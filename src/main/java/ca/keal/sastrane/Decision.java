package ca.keal.sastrane;

import ca.keal.sastrane.util.Resource;
import lombok.NonNull;

/**
 * An interface representing a decision, such as a pawn promotion in chess.
 * <p>
 * This interface is designed to be used as an <a href="http://stackoverflow.com/a/2709831">extensible enum</a>.
 */
public interface Decision {
    
    void onChoose(@NonNull Round round);
    
    @NonNull
    Resource getIcon();
    
    @NonNull
    String getName();
    
}