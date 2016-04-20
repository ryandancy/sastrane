package ca.keal.sastrane;

import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.event.TurnEvent;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
public class Game {
    
    private final RuleSet ruleSet;
    private final Map<Combatant, Player> combatantsToPlayers;
    private final Board board;
    
    private int move = 0;
    
    public Game(@NonNull RuleSet ruleSet, @NonNull Map<Combatant, Player> combatantsToPlayers) {
        if (!Util.areElementsEqual(combatantsToPlayers.keySet(), ruleSet.getCombatants())) {
            throw new IllegalArgumentException("Game: players' getCombatants()s must = ruleSet.getCombatants()");
        }
        this.ruleSet = ruleSet;
        this.board = ruleSet.getBoardBuilder().build();
        this.combatantsToPlayers = ImmutableMap.copyOf(combatantsToPlayers);
    }
    
    public void nextTurn() {
        ruleSet.getBus().post(new TurnEvent.Pre(this));
        
        // We could index turn (as a field) in players and add 1... but ctp.size() % move works better + faster
        Player turn = combatantsToPlayers.get(ruleSet.getCombatants().get(combatantsToPlayers.size() % move));
        Move turnMove = turn.getMove(this);
        
        ruleSet.getBus().post(new MoveEvent.Pre(this, turnMove));
        turnMove.move(board);
        ruleSet.getBus().post(new MoveEvent.Post(this, turnMove));
        
        ruleSet.getBus().post(new TurnEvent.Post(this));
        move++;
    }
    
    public Combatant getCurrentTurn() {
        return ruleSet.getCombatants().get(combatantsToPlayers.size() % move);
    }
    
}