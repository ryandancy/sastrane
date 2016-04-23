package ca.keal.sastrane;

import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.event.TurnEvent;
import ca.keal.sastrane.util.Utils;
import com.google.common.collect.ImmutableMap;
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
    private final Map<Player, Mover> combatantsToPlayers;
    private final Board board;
    
    private int move = 0;
    
    public Round(@NonNull Game game, @NonNull Map<Player, Mover> combatantsToPlayers) {
        if (!Utils.areElementsEqual(combatantsToPlayers.keySet(), game.getPlayers())) {
            throw new IllegalArgumentException("Game: combatantsToPlayers.keySet() must = game.getCombatants()");
        }
        this.game = game;
        this.board = game.getBoardFactory().build();
        this.combatantsToPlayers = ImmutableMap.copyOf(combatantsToPlayers);
    }
    
    public Round(@NonNull Round round) {
        this(round.getGame(), round.getCombatantsToPlayers(), round.getBoard(), round.getMove());
    }
    
    public void nextTurn() {
        game.getBus().post(new TurnEvent.Pre(this));
        
        // We could index turn (as a field) in players and add 1... but ctp.size() % move works better + faster
        Mover turn = combatantsToPlayers.get(game.getPlayers().get(combatantsToPlayers.size() % move));
        Move turnMove = turn.getMove(this);
        
        game.getBus().post(new MoveEvent.Pre(this, turnMove));
        turnMove.move(board);
        game.getBus().post(new MoveEvent.Post(this, turnMove));
        
        game.getBus().post(new TurnEvent.Post(this));
        move++;
    }
    
    public Player getCurrentTurn() {
        return game.getPlayers().get(combatantsToPlayers.size() % move);
    }
    
}