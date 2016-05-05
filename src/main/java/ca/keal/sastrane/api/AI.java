package ca.keal.sastrane.api;

import ca.keal.sastrane.api.move.Move;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The base class of all AIs for all games. Uses the <a href="https://en.wikipedia.org/wiki/Minimax">minimax</a>
 * algorithm combined with <a href="https://en.wikipedia.org/wiki/Alpha-beta_pruning">alpha-beta pruning</a>.
 */
@Getter
@RequiredArgsConstructor
public abstract class AI implements Mover {
    
    /** 1 >= difficulty >= 0 */
    private final double difficulty;
    
    private double minimaxAlphaBeta(Round round, int depth, Player player) {
        return minimaxAlphaBeta(depth, Double.MIN_VALUE, Double.MAX_VALUE, true, round, player);
    }
    
    private double minimaxAlphaBeta(Round round, Move move, int depth, Player player) {
        return minimaxAlphaBeta(depth, Double.MIN_VALUE, Double.MAX_VALUE, true, round.copyWithMove(move), player);
    }
    
    // https://en.wikipedia.org/wiki/Alpha-beta_pruning#Pseudocode
    private double minimaxAlphaBeta(int depth, double a, double b, boolean maximizingPlayer, Round round,
                                    Player player) {
        if (depth == 0) {
            return heuristic(round, player);
        }
        
        List<Move> moves = round.getAllPossibleMoves(player);
        if (moves.size() == 0) {
            return heuristic(round, player);
        }
        
        if (maximizingPlayer) {
            double v = Double.MIN_VALUE;
            for (Move move : moves) {
                Round roundCopy = round.copyWithMove(move);
                v = Math.max(v, minimaxAlphaBeta(depth - 1, a, b, false, roundCopy, player));
                a = Math.max(a, v);
                if (b <= a) break;
            }
            return v;
        } else {
            double v = Double.MAX_VALUE;
            for (Move move : moves) {
                Round roundCopy = round.copyWithMove(move);
                v = Math.min(v, minimaxAlphaBeta(depth - 1, a, b, true, roundCopy, player));
                b = Math.min(b, v);
                if (b <= a) break;
            }
            return v;
        }
    }
    
    @Override
    public Move getMove(Round round, Player player) {
        return round.getAllPossibleMoves(player).stream()
                .max(Comparator.comparing(move -> minimaxAlphaBeta(round, move, getDepth(), player),
                        Double::compare))
                .get();
    }
    
    @Override
    public Decision decide(Decision[] options, Round round, Player player) {
        return Arrays.stream(options)
                .max(Comparator.comparing(option -> minimaxAlphaBeta(option.whatIf(round), getDepth(), player),
                        Double::compare))
                .get();
    }
    
    /** depth = 3*difficulty + 1 */
    private int getDepth() {
        return (int) (3 * difficulty) + 1;
    }
    
    protected abstract double heuristic(Round round, Player player);
    
}