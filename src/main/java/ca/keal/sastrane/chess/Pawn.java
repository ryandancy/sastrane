package ca.keal.sastrane.chess;

import ca.keal.sastrane.Board;
import ca.keal.sastrane.Move;
import ca.keal.sastrane.MovingMove;
import ca.keal.sastrane.MovingPiece;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Utils;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Pawn implements MovingPiece {
    
    // TODO: add MoveEvent event handler, check for double step by pawn, set, reset, hasMoved
    private boolean lastMoveDouble;
    private boolean moved;
    
    @Override
    @NonNull
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos,
                                       @NonNull Player allegiance) {
        List<Move> moves = new ArrayList<>();
        
        // One in front if not occupied; two in front if first and not occupied
        for (int n = 1; n <= (moved ? 1 : 2); n++) {
            Square nInFront = boardPos.withY(boardPos.getY() + n);
            // round.getBoard().isOn(nInFront) shouldn't be possible b/c promotions... handle as special case???
            if (round.getBoard().isOn(nInFront) && round.getBoard().get(nInFront) == null) {
                moves.add(boardPos.to(nInFront));
            }
        }
        
        // Diagonally if occupied by opposite player
        for (int i = -1; i <= 1; i += 2) {
            Square diagonal = new Square(boardPos.getX() + i, boardPos.getY() + 1);
            if (round.getBoard().isOn(diagonal)) {
                Pair<Piece, Player> diagonalOnBoard = round.getBoard().get(diagonal);
                if (diagonalOnBoard != null && allegiance != diagonalOnBoard.getRight()) {
                    moves.add(boardPos.to(diagonal));
                }
            }
        }
        
        // En passent (https://en.wikipedia.org/wiki/En_passent)
        for (int i = -1; i <= 1; i += 2) {
            // Check for opposite-allegiance pawn at side that just moved a double step
            Square side = boardPos.withX(boardPos.getX() + i);
            if (round.getBoard().isOn(side)) {
                Pair<Piece, Player> sidePiecePlayer = round.getBoard().get(side);
                if (allegiance != sidePiecePlayer.getRight() && sidePiecePlayer.getLeft() instanceof Pawn
                        && ((Pawn) sidePiecePlayer.getLeft()).isLastMoveDouble()) {
                    // Check that the diagonal on that side is clear
                    Square diagonal = new Square(boardPos.getX() + i, boardPos.getY() + 1);
                    // round.getBoard().isOn(diagonal) should always be true - simplify???
                    if (round.getBoard().isOn(diagonal) && round.getBoard().get(diagonal) == null) {
                        // Use MovingMove subclass to kill side piece when moved
                        moves.add(new MovingMove(boardPos, diagonal) {
                            @Override
                            public void move(@NonNull Board board) {
                                super.move(board);
                                board.set(side, null);
                            }
                        });
                    }
                }
            }
        }
        
        return Utils.perspectivizeAll(moves, allegiance);
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO Pawn's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_pawn");
    }
    
    @Subscribe
    public void afterMove(MoveEvent.Post e) {
        Square endPos = e.getMove().getEndPos();
        Pair<Piece, Player> atEndPos = e.getRound().getBoard().get(endPos);
        if (atEndPos != null && atEndPos.getLeft() instanceof Pawn
                && (endPos.getY() == 0 || endPos.getY() == e.getRound().getBoard().getMaxY())) {
            e.getMover().decide(PromotionDecision.class).onChoose(e.getRound());
        }
    }
    
}