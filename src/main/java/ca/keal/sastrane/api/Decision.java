package ca.keal.sastrane.api;

import ca.keal.sastrane.util.Resource;

/**
 * An interface representing a decision, such as a pawn promotion in chess.
 * <p>
 * This interface is designed to be used as an <a href="http://stackoverflow.com/a/2709831">extensible enum</a>.
 */
public interface Decision {
    
    void onChoose(Round round);
    
    default Round whatIf(Round round) {
        Round newRound = new Round(round);
        onChoose(newRound);
        return newRound;
    }
    
    Resource getIcon();
    
    String getI18nName();
    
}