package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Pair;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class Rook extends LinePiece {
    
    private int numMoves = 0;
    
    public Rook() {
        super(UP | LEFT | DOWN | RIGHT);
        Chess.getInstance().getBus().register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        return KingInCheckUtils.getPossibleMoves(this, round, boardPos, player);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Rook's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_rook");
    }
    
    // this is extremely boilerplate (& repeated in King, Pawn), figure out some solution???
    @Subscribe
    public void afterMove(MoveEvent.Post e) {
        Pair<Piece, Player> atEndPos = e.getRound().getBoard().get(e.getMove().getEndPos());
        if (atEndPos != null && atEndPos.getLeft() instanceof Rook) {
            ((Rook) atEndPos.getLeft()).numMoves++;
        }
    }
    
}