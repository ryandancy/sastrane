package ca.keal.sastrane;

import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.event.TurnEvent;
import ca.keal.sastrane.util.Utils;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
public class Round {
    
    private final Game game;
    private final Map<Combatant, Player> combatantsToPlayers;
    private final Board board;
    
    private int move = 0;
    
    public Round(@NonNull Game game, @NonNull Map<Combatant, Player> combatantsToPlayers) {
        if (!Utils.areElementsEqual(combatantsToPlayers.keySet(), game.getCombatants())) {
            throw new IllegalArgumentException("Game: combatantsToPlayers.keySet() must = game.getCombatants()");
        }
        this.game = game;
        this.board = game.getBoardFactory().create();
        this.combatantsToPlayers = ImmutableMap.copyOf(combatantsToPlayers);
    }
    
    public void nextTurn() {
        game.getBus().post(new TurnEvent.Pre(this));
        
        // We could index turn (as a field) in players and add 1... but ctp.size() % move works better + faster
        Player turn = combatantsToPlayers.get(game.getCombatants().get(combatantsToPlayers.size() % move));
        Move turnMove = turn.getMove(this);
        
        game.getBus().post(new MoveEvent.Pre(this, turnMove));
        turnMove.move(board);
        game.getBus().post(new MoveEvent.Post(this, turnMove));
        
        game.getBus().post(new TurnEvent.Post(this));
        move++;
    }
    
    public Combatant getCurrentTurn() {
        return game.getCombatants().get(combatantsToPlayers.size() % move);
    }
    
}