package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * All subclasses must create instances of themselves on load; most will implement this as a singleton.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Game {
    
    private static final List<Game> GAMES = new ArrayList<>();
    
    private final String name;
    private final String i18nName;
    private final Resource icon;
    private final Resource css;
    private final Player[] players; // TODO support for variable-player games (are those a thing?)
    private final Function<Double, AI> ai;
    private final Board.Factory boardFactory;
    private final boolean placeOnly;
    private final PlacingPiece[] placingPieces;
    private EventBus bus;
    
    /**
     * {@code name} is both the name of this game's associated {@link EventBus} and also the ResourceBundle base name.
     * {@code players} should be in the order in which the players move (e.g. for chess, {@code [White, Black]}). {@code
     * css} should be a css file, which will be applied to this game's tile, new-game screen, player settings things,
     * and game screen. {@code placeOnly} is whether this game is place-only (i.e. it has no moving pieces).
     */
    public Game(String name, String i18nName, Resource icon, Resource css, Player[] players, Function<Double, AI> ai,
                Board.Factory boardFactory, boolean placeOnly, PlacingPiece... placingPieces) {
        this(name, i18nName, icon, css, players, ai, boardFactory, placeOnly, placingPieces, new EventBus(name));
        registerGame(this);
        I18n.load(name);
    }
    
    /**
     * Same as {@link #Game(String, String, Resource, Resource, Player[], Function, Board.Factory, boolean,
     * PlacingPiece...)} except that {@code placeOnly} defaults to {@code placingPieces.length > 0}.
     */
    public Game(String name, String i18nName, Resource icon, Resource css, Player[] players, Function<Double, AI> ai,
                Board.Factory boardFactory, PlacingPiece... placingPieces) {
        this(name, i18nName, icon, css, players, ai, boardFactory, placingPieces.length > 0, placingPieces);
    }
    
    private static void registerGame(Game game) {
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
    
    public abstract Result getResult(Round round);
    
}