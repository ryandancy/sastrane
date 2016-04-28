package ca.keal.sastrane;

import ca.keal.sastrane.util.Resource;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * All subclasses must create instances of themselves on load; most will implement this as a singleton.
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class Game {
    
    private static final List<Game> GAMES = new ArrayList<>();
    
    private final String name; // TODO i18n
    private final Resource icon;
    private final Player[] players; // TODO support for variable-player games (are those a thing?)
    private final Board.Factory boardFactory;
    private final PlacingPiece[] placingPieces;
    private EventBus bus;
    
    /**
     * {@code players} should be in the order in which the players move (e.g. for chess, {@code [White, Black]}).
     */
    public Game(@NonNull String name, @NonNull Resource icon, @NonNull Player[] players,
                @NonNull Board.Factory boardFactory, @NonNull PlacingPiece... placingPieces) {
        this.name = name;
        this.icon = icon;
        this.players = players;
        this.boardFactory = boardFactory;
        this.placingPieces = placingPieces;
        bus = new EventBus(name);
        registerGame(this);
    }
    
    public static void registerGame(Game game) {
        GAMES.add(game);
    }
    
    public static List<Game> getGames() {
        return ImmutableList.copyOf(GAMES);
    }
    
    /**
     * Throws away the old bus and replaces it with a new one; has the effect of unregistering all subscribers.
     */
    public void refreshBus() {
        bus = new EventBus(name);
    }
    
}