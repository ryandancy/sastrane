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

// Would be called "Game" (as in "the game of chess") except Game represents a game as in "a game of chess"
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RuleSet {
    
    private static final List<RuleSet> RULE_SETS = new ArrayList<>();
    
    private final String name;
    private final List<Combatant> combatants; // TODO support for variable-player games (are those a thing?)
    private final BoardFactory boardFactory;
    
    @NonNull
    private final EventBus bus;
    
    /**
     * {@code combatants} should be in the order in which the combatants move (e.g. for chess, {@code [White, Black]}).
     */
    public RuleSet(@NonNull String name, @NonNull List<Combatant> combatants, @NonNull BoardFactory boardFactory) {
        this(name, ImmutableList.copyOf(combatants), boardFactory, new EventBus(name));
    }
    
    public static void registerRuleSet(RuleSet ruleSet) {
        RULE_SETS.add(ruleSet);
    }
    
    public static List<RuleSet> getRuleSets() {
        return ImmutableList.copyOf(RULE_SETS);
    }
    
}