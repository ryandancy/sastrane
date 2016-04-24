package ca.keal.sastrane;

import ca.keal.sastrane.util.Pair;
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
    
    private final String name; // TODO i18n
    private final Pair<String, String> iconPackageAndName;
    private final Player[] players; // TODO support for variable-player games (are those a thing?)
    private final Board.Factory boardFactory;
    private final EventBus bus;
    
    /**
     * {@code players} should be in the order in which the players move (e.g. for chess, {@code [White, Black]}).
     */
    public Game(@NonNull String name, @NonNull Pair<String, String> iconPackageAndName, @NonNull Player[] players,
                @NonNull Board.Factory boardFactory) {
        this(name, iconPackageAndName, players, boardFactory, new EventBus(name));
    }
    
    public static void registerGame(Game game) {
        GAMES.add(game);
    }
    
    public static List<Game> getGames() {
        return ImmutableList.copyOf(GAMES);
    }
    
}