package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.MoveEvent;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Doesn't implement MoveCountingPiece because it handles MoveEvent.Post itself
@Getter
public class Pawn implements RecursiveMovingPiece {
    
    private boolean lastMoveDouble;
    private int numMoves = 0;
    
    public Pawn() {
        Chess.getInstance().getBus().register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return getPossibleMovesNonRecursive(round, boardPos, player).stream()
                .filter(KingInCheckUtils.checkKing(round, player))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos,
                                                   Player player) {
        List<Move> moves = new ArrayList<>();
        
        // One in front if not occupied; two in front if first and not occupied
        for (int n = 1; n <= (numMoves == 0 ? 2 : 1); n++) {
            Square nInFront = player.perspectivize(boardPos.withY(boardPos.getY() + n), boardPos);
            // !round.getBoard().isOn(nInFront) shouldn't be possible b/c promotions... handle as special case???
            if (round.getBoard().isOn(nInFront) && round.getBoard().get(nInFront) == null) {
                moves.add(boardPos.to(nInFront));
            }
        }
        
        // Diagonally if occupied by opposite player
        for (int i = -1; i <= 1; i += 2) {
            Square diagonal = player.perspectivize(new Square(boardPos.getX() + i, boardPos.getY() + 1), boardPos);
            if (!round.getBoard().isOn(diagonal)) continue;
            
            OwnedPiece diagonalOnBoard = round.getBoard().get(diagonal);
            if (diagonalOnBoard != null && player != diagonalOnBoard.getOwner()) {
                moves.add(boardPos.to(diagonal));
            }
        }
        
        // En passent (https://en.wikipedia.org/wiki/En_passent)
        for (int i = -1; i <= 1; i += 2) {
            // Check for opposite-player pawn at side that just moved a double step
            Square side = player.perspectivize(boardPos.withX(boardPos.getX() + i), boardPos);
            if (!round.getBoard().isOn(side)) continue;
            
            OwnedPiece sidePiecePlayer = round.getBoard().get(side);
            if (sidePiecePlayer == null
                    || player == sidePiecePlayer.getOwner()
                    || !(sidePiecePlayer.getPiece() instanceof Pawn)
                    || !((Pawn) sidePiecePlayer.getPiece()).isLastMoveDouble()) {
                continue;
            }
            
            // Check that the diagonal on that side is clear
            Square diagonal = player.perspectivize(new Square(boardPos.getX() + i, boardPos.getY() + 1), boardPos);
            // round.getBoard().isOn(diagonal) should always be true - simplify???
            if (round.getBoard().isOn(diagonal) && round.getBoard().get(diagonal) == null) {
                // Use MovingMove subclass to kill side piece when moved
                moves.add(new MovingMove(boardPos, diagonal) {
                    @Override
                    public void move(Board board) {
                        super.move(board);
                        board.set(side, null);
                    }
                });
            }
        }
        
        return moves;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.piece", "pawn.png");
    }
    
    @Subscribe
    public void afterMove(MoveEvent.Post e) {
        Square endPos = e.getMove().getEndPos();
        OwnedPiece atEndPos = e.getRound().getBoard().get(endPos);
        if (atEndPos != null && atEndPos.getPiece() instanceof Pawn) {
            // Promotion
            if (endPos.getY() == 0 || endPos.getY() == e.getRound().getBoard().getMaxY()) {
                e.getMover().decide(PromotionDecision.values(), e.getRound(), atEndPos.getOwner())
                        .onChoose(e.getRound());
            }
            
            Pawn pawn = (Pawn) atEndPos.getPiece();
            pawn.numMoves++;
            // last move is double if this is its first move and it's on the 3rd/maxyY-3rd rank (with zeroth rank)
            pawn.lastMoveDouble = pawn.numMoves == 1
                    && (endPos.getY() == 3 || endPos.getY() == e.getRound().getBoard().getMaxY() - 3);
        }
    }
    
}