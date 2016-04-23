package ca.keal.sastrane;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * All subclasses <strong>MUST</strong> have a no-arguments constructor!
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Game {
    
    private static final List<Game> GAMES = new ArrayList<>();
    
    private final String name;
    private final String iconURL;
    private final List<Player> players; // TODO support for variable-player games (are those a thing?)
    private final Board.Factory boardFactory;
    private final EventBus bus;
    
    /**
     * {@code combatants} should be in the order in which the combatants move (e.g. for chess, {@code [White, Black]}).
     */
    public Game(@NonNull String name, @NonNull String iconURL, @NonNull List<Player> players,
                @NonNull Board.Factory boardFactory) {
        this(name, iconURL, ImmutableList.copyOf(players), boardFactory, new EventBus(name));
    }
    
    public static void registerGame(Game game) {
        GAMES.add(game);
    }
    
    public static List<Game> getGames() {
        return ImmutableList.copyOf(GAMES);
    }
    
}