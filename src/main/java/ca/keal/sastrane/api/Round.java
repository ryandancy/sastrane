package ca.keal.sastrane.api;

import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.api.event.MoveEvent;
import ca.keal.sastrane.api.event.RoundEvent;
import ca.keal.sastrane.api.event.TurnEvent;
import ca.keal.sastrane.api.event.WinEvent;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Utils;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Round {
    
    private final Game game;
    private final Map<Player, Mover> playersToMovers;
    private final Board board;
    
    private int moveNum = 0;
    private boolean ended = false;
    
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Deque<Move> moves = new ArrayDeque<>();
    
    public Round(@NonNull Game game, @NonNull Map<Player, Mover> playersToMovers) {
        if (!Utils.areElementsEqual(playersToMovers.keySet(), Arrays.asList(game.getPlayers()))) {
            throw new IllegalArgumentException("Round: playersToMovers.keySet() must = game.getCombatants()");
        }
        this.game = game;
        this.playersToMovers = ImmutableMap.copyOf(playersToMovers);
        this.board = game.getBoardFactory().build();
        game.getBus().register(this);
    }
    
    public Round(@NonNull Round round) {
        this(round.getGame(), ImmutableMap.copyOf(round.getPlayersToMovers()), new Board(round.getBoard()),
                round.getMoveNum(), round.isEnded(), new ArrayDeque<>(round.getMoves()));
    }
    
    public void nextTurn() {
        if (ended) throw new IllegalStateException("nextTurn() cannot be called on a Round that is already ended");
        game.getBus().post(new TurnEvent.Pre(this));
        
        Player player = getCurrentTurn();
        Mover mover = playersToMovers.get(player);
        Move move = mover.getMove(this, player);
        
        game.getBus().post(new MoveEvent.Pre(this, move));
        move.move(board);
        moves.add(move);
        game.getBus().post(new MoveEvent.Post(this, move));
        
        game.getBus().post(new TurnEvent.Post(this));
        moveNum++;
    }
    
    public void start() {
        game.getBus().post(new RoundEvent.Pre(this));
        while (!ended) {
            nextTurn();
        }
        game.getBus().post(new RoundEvent.Post(this));
        game.refreshBus();
    }
    
    public Player getCurrentTurn() {
        return game.getPlayers()[moveNum % game.getPlayers().length];
    }
    
    @NonNull
    public Round copyWithMove(@NonNull Move move) {
        Round newRound = new Round(this);
        move.move(newRound.getBoard());
        return newRound;
    }
    
    /** player == null => any player */
    @NonNull
    public List<Move> getAllPossibleMoves(Player player) {
        List<Move> moves = new ArrayList<>();
        for (Square square : board) {
            Pair<Piece, Player> atSquare = board.get(square);
            if (atSquare != null && atSquare.getLeft() instanceof MovingPiece
                    && (player == null || atSquare.getRight() == player)) {
                moves.addAll(((MovingPiece) atSquare.getLeft()).getPossibleMoves(this, square, atSquare.getRight()));
            }
        }
        for (PlacingPiece placingPiece : game.getPlacingPieces()) {
            moves.addAll(placingPiece.getPossiblePlacements(this, player));
        }
        return moves;
    }
    
    @Subscribe
    public void onWin(WinEvent e) {
        ended = true;
    }
    
}