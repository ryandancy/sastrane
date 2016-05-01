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
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Doesn't implement MoveCountingPiece because it handles MoveEvent.Post itself
@Getter
public class Pawn implements MovingPiece {
    
    private boolean lastMoveDouble;
    private int numMoves = 0;
    
    public Pawn() {
        Chess.getInstance().getBus().register(this);
    }
    
    @Override
    @NonNull
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos,
                                       @NonNull Player player) {
        List<Move> moves = new ArrayList<>();
        
        // One in front if not occupied; two in front if first and not occupied
        for (int n = 1; n <= (numMoves == 0 ? 2 : 1); n++) {
            Square nInFront = player.perspectivize(boardPos.withY(boardPos.getY() + n), boardPos);
            // round.getBoard().isOn(nInFront) shouldn't be possible b/c promotions... handle as special case???
            if (round.getBoard().isOn(nInFront) && round.getBoard().get(nInFront) == null) {
                moves.add(boardPos.to(nInFront));
            }
        }
        
        // Diagonally if occupied by opposite player
        for (int i = -1; i <= 1; i += 2) {
            Square diagonal = player.perspectivize(new Square(boardPos.getX() + i, boardPos.getY() + 1), boardPos);
            if (round.getBoard().isOn(diagonal)) {
                Pair<Piece, Player> diagonalOnBoard = round.getBoard().get(diagonal);
                if (diagonalOnBoard != null && player != diagonalOnBoard.getRight()) {
                    moves.add(boardPos.to(diagonal));
                }
            }
        }
        
        // En passent (https://en.wikipedia.org/wiki/En_passent)
        for (int i = -1; i <= 1; i += 2) {
            // Check for opposite-player pawn at side that just moved a double step
            Square side = player.perspectivize(boardPos.withX(boardPos.getX() + i), boardPos);
            if (round.getBoard().isOn(side)) {
                Pair<Piece, Player> sidePiecePlayer = round.getBoard().get(side);
                if (player != sidePiecePlayer.getRight() && sidePiecePlayer.getLeft() instanceof Pawn
                        && ((Pawn) sidePiecePlayer.getLeft()).isLastMoveDouble()) {
                    // Check that the diagonal on that side is clear
                    Square diagonal = player.perspectivize(new Square(boardPos.getX() + i, boardPos.getY() + 1),
                            boardPos);
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
        
        return moves.stream()
                .filter(KingInCheckUtils.checkKing(round, player))
                .collect(Collectors.toList());
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.icon", "pawn.png");
    }
    
    @Subscribe
    public void afterMove(MoveEvent.Post e) {
        Square endPos = e.getMove().getEndPos();
        Pair<Piece, Player> atEndPos = e.getRound().getBoard().get(endPos);
        if (atEndPos != null && atEndPos.getLeft() instanceof Pawn) {
            // Promotion
            if (endPos.getY() == 0 || endPos.getY() == e.getRound().getBoard().getMaxY()) {
                e.getMover().decide(PromotionDecision.values(), e.getRound(), atEndPos.getRight())
                        .onChoose(e.getRound());
            }
            
            Pawn pawn = (Pawn) atEndPos.getLeft();
            pawn.numMoves++;
            // last move is double if this is its first move and it's on the 3rd/maxyY-3rd rank (with zeroth rank)
            pawn.lastMoveDouble = pawn.numMoves == 1
                    && (endPos.getY() == 3 || endPos.getY() == e.getRound().getBoard().getMaxY() - 3);
        }
    }
    
}