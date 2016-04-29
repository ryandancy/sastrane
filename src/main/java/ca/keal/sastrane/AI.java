package ca.keal.sastrane;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The base class of all AIs for all games. Uses the <a href="https://en.wikipedia.org/wiki/Minimax">minimax</a>
 * algorithm combined with <a href="https://en.wikipedia.org/wiki/Alpha-beta_pruning">alpha-beta pruning</a>.
 */
@Getter
@RequiredArgsConstructor
public abstract class AI implements Mover {
    
    /** 1 >= difficulty >= 0 */
    private final double difficulty;
    
    public double minimaxAlphaBeta(@NonNull Round round, int depth, @NonNull Player player) {
        return minimaxAlphaBeta(new MoveTreeNode(round, player), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true,
                player);
    }
    
    public double minimaxAlphaBeta(@NonNull Round round, @NonNull Move move, int depth, @NonNull Player player) {
        return minimaxAlphaBeta(new MoveTreeNode(round, move, player), depth, Double.MIN_VALUE, Double.MAX_VALUE,
                true, player);
    }
    
    // https://en.wikipedia.org/wiki/Alpha-beta_pruning#Pseudocode
    private double minimaxAlphaBeta(@NonNull MoveTreeNode node, int depth, double a, double b,
                                    boolean maximizingPlayer, @NonNull Player player) {
        if (depth == 0 || node.isTerminal()) {
            return heuristic(node.getRound(), player);
        }
        
        if (maximizingPlayer) {
            double v = Double.MIN_VALUE;
            for (MoveTreeNode child : node) {
                v = Math.max(v, minimaxAlphaBeta(child, depth - 1, a, b, false, player));
                a = Math.max(a, v);
                if (b <= a) break;
            }
            return v;
        } else {
            double v = Double.MAX_VALUE;
            for (MoveTreeNode child : node) {
                v = Math.min(v, minimaxAlphaBeta(child, depth - 1, a, b, true, player));
                b = Math.min(b, v);
                if (b <= a) break;
            }
            return v;
        }
    }
    
    @Override
    @NonNull
    public Move getMove(@NonNull Round round, @NonNull Player player) {
        return round.getAllPossibleMoves(player).stream()
                .max(Comparator.comparing(move -> minimaxAlphaBeta(round, move, getDepth(), player),
                        Double::compare))
                .get();
    }
    
    @Override
    @NonNull
    public Decision decide(@NonNull Decision[] options, @NonNull Round round, @NonNull Player player) {
        return Arrays.stream(options)
                .max(Comparator.comparing(option -> minimaxAlphaBeta(option.whatIf(round), getDepth(), player),
                        Double::compare))
                .get();
    }
    
    /** depth = 5*difficulty + 3 */
    public int getDepth() {
        return (int) (5 * difficulty) + 3;
    }
    
    public abstract double heuristic(@NonNull Round round, @NonNull Player player);
    
    @Getter
    public static class MoveTreeNode implements Iterable<MoveTreeNode> {
        
        private final Round round;
        private final List<Move> childMoves;
        private final Player player;
        
        public MoveTreeNode(@NonNull Round round, @NonNull Player player) {
            this.round = round;
            this.player = player;
            this.childMoves = round.getAllPossibleMoves(player);
        }
        
        public MoveTreeNode(@NonNull Round round, @NonNull Move move, @NonNull Player player) {
            this.round = round.copyWithMove(move);
            this.player = player;
            this.childMoves = this.round.getAllPossibleMoves(player);
        }
        
        public boolean isTerminal() {
            return childMoves.size() == 0;
        }
        
        @NonNull
        public List<MoveTreeNode> getChildren() {
            return childMoves.stream()
                    .map(move -> new MoveTreeNode(round, move, player))
                    .collect(Collectors.toList());
        }
        
        @Override
        @NonNull
        public Iterator<MoveTreeNode> iterator() {
            return this.getChildren().iterator();
        }
        
    }
    
}