package ca.keal.sastrane.chess;

import ca.keal.sastrane.Board;
import ca.keal.sastrane.Move;
import ca.keal.sastrane.MoveCountingPiece;
import ca.keal.sastrane.MovingMove;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.RecursiveMovingPiece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.util.Utils;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class King implements RecursiveMovingPiece, MoveCountingPiece {
    
    private int numMoves = 0;
    
    public King() {
        Chess.getInstance().getBus().register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        // All 8 surrounding squares that aren't in the path of any player other than player
        // It'd be a shame to ruin this beautifullly formatted piece of code, Intellij formatter: @formatter:off
        return Stream.concat(
                    getPossibleMovesNonRecursive(round, boardPos, player).stream(),
                    getCastlingMoves(round, boardPos, player).stream())
                .filter(KingInCheckUtils.checkKing(round, player))
                .collect(Collectors.toList());
    } // @f:y
    
    // https://en.wikipedia.org/wiki/Castling#Requirements
    private List<Move> getCastlingMoves(Round round, Square boardPos, Player player) {
        // Don't need to check for king on first rank because numMoves == 0 implies so
        if (numMoves != 0 || Utils.canBeMovedTo(round, boardPos, player)) return new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        
        for (int rookX = 0; rookX <= round.getBoard().getMaxX(); rookX += round.getBoard().getMaxX()) {
            if (canCastle(round, boardPos, player, rookX)) {
                // Use MovingMove subclass to move rook too
                int inc = (int) Math.signum(boardPos.getX() - rookX);
                final int rookXFinal = rookX; // required because rookX isn't effectively final
                
                moves.add(new MovingMove(boardPos, player.perspectivize(boardPos.withX(boardPos.getX() + 2 * inc), 
                        boardPos)) {
                    @Override
                    public void move(@NonNull Board board) {
                        super.move(board);
                        
                        Square rookPos = boardPos.withX(rookXFinal);
                        board.set(boardPos.withX(boardPos.getX() + inc), board.get(rookPos));
                        board.set(rookPos, null);
                    }
                });
            }
        }
        
        return moves;
    }
    
    private boolean canCastle(Round round, Square boardPos, Player player, int rookX) {
        Pair<Piece, Player> pieceAtX = round.getBoard().get(player.perspectivize(boardPos.withX(rookX), boardPos));
        if (pieceAtX == null || !(pieceAtX.getLeft() instanceof Rook)) return false;
        Rook rook = (Rook) pieceAtX.getLeft();
        if (rook.getNumMoves() != 0) return false;
        
        // Ensure there are no pieces between rook & king
        int inc = (int) Math.signum(boardPos.getX() - rookX);
        for (int x = boardPos.getX() + inc; x != rookX; x += inc) {
            Square between = player.perspectivize(boardPos.withX(x), boardPos);
            if (!round.getBoard().isOn(between) || round.getBoard().get(between) != null) return false;
        }
        
        // Ensure that the position the king will 'pass through' is not in check
        return Utils.canBeMovedTo(round, player.perspectivize(boardPos.withX(boardPos.getX() + inc), boardPos), player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(@NonNull Round round, @NonNull Square boardPos,
                                                   @NonNull Player allegiance) {
        // All 8 surrounding squares
        List<Move> moves = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Square square = allegiance.perspectivize(new Square(x, y), boardPos);
                if (!(x == 0 && y == 0) && round.getBoard().isOn(square)) {
                    moves.add(boardPos.to(square));
                }
            }
        }
        return moves;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.icon", "king.png");
    }
    
    @Override
    public void incrementMoveCount() {
        numMoves++;
    }
    
}