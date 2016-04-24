package ca.keal.sastrane;

import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.event.RoundEvent;
import ca.keal.sastrane.event.TurnEvent;
import ca.keal.sastrane.event.WinEvent;
import ca.keal.sastrane.util.Utils;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Round {
    
    private final Game game;
    private final Map<Player, Mover> playersToMovers;
    private final Board board;
    
    private int move = 0;
    private boolean ended = false;
    
    public Round(@NonNull Game game, @NonNull Map<Player, Mover> playersToMovers) {
        if (!Utils.areElementsEqual(playersToMovers.keySet(), game.getPlayers())) {
            throw new IllegalArgumentException("Round: playersToMovers.keySet() must = game.getCombatants()");
        }
        this.game = game;
        this.playersToMovers = ImmutableMap.copyOf(playersToMovers);
        this.board = game.getBoardFactory().build();
        game.getBus().register(this);
    }
    
    public Round(@NonNull Round round) {
        this(round.getGame(), round.getPlayersToMovers(), new Board(round.getBoard()), round.getMove(),
                round.isEnded());
    }
    
    public void nextTurn() {
        if (ended) throw new IllegalStateException("nextTurn() cannot be called on a Round that is already ended");
        
        if (move == 0) {
            game.getBus().post(new RoundEvent.Pre(this));
        }
        
        game.getBus().post(new TurnEvent.Pre(this));
        
        // We could index turn (as a field) in players and add 1... but ptm.size() % move works better + faster
        Mover turn = playersToMovers.get(game.getPlayers().get(playersToMovers.size() % move));
        Move turnMove = turn.getMove(this);
        
        game.getBus().post(new MoveEvent.Pre(this, turnMove));
        turnMove.move(board);
        game.getBus().post(new MoveEvent.Post(this, turnMove));
        
        game.getBus().post(new TurnEvent.Post(this));
        move++;
    }
    
    public Player getCurrentTurn() {
        return game.getPlayers().get(playersToMovers.size() % move);
    }
    
    @NonNull
    public Round copyWithMove(@NonNull Move move) {
        Round newRound = new Round(this);
        move.move(newRound.getBoard());
        return newRound;
    }
    
    @Subscribe
    public void onWin(WinEvent e) {
        ended = true;
        game.getBus().unregister(this);
    }
    
}