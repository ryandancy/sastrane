package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * An event posted when a game is won. Game implementations should post this when the winning criteria for the game have
 * been met.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WinEvent extends RoundEvent {
    
    /**
     * The winner of the game. {@code null} means the game was a tie.
     */
    private final Player winner;
    
    public WinEvent(Round round, Player winner) {
        super(round);
        this.winner = winner;
    }
    
}