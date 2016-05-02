package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum PromotionDecision implements Decision {
    
    // TODO find a way to refer to piece classes' icons? i18n?
    QUEEN(new Resource("ca.keal.sastrane.chess.piece", "queen_white.png"), "Promote to Queen", Queen::new),
    ROOK(new Resource("ca.keal.sastrane.chess.piece", "rook_white.png"), "Promote to Rook", Rook::new),
    BISHOP(new Resource("ca.keal.sastrane.chess.piece", "bishop_white.png"), "Promote to Bishop", Bishop::new),
    KNIGHT(new Resource("ca.keal.sastrane.chess.piece", "knight_white.png"), "Promote to Knight", Knight::new);
    
    @Getter
    private final Resource icon;
    
    @Getter
    private final String name;
    
    private final Supplier<Piece> pieceSupplier;
    
    @Override
    public void onChoose(Round round) {
        Square lastMovePos = round.getMoves().peekLast().getEndPos();
        // If there's nothing at lastMovePos, we'll get an NPE - but that should be impossible
        //noinspection ConstantConditions
        round.getBoard().set(lastMovePos, new OwnedPiece(pieceSupplier.get(),
                round.getBoard().get(lastMovePos).getOwner()));
    }
    
}