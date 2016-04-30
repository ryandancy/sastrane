package ca.keal.sastrane.chess;

import ca.keal.sastrane.Decision;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum PromotionDecision implements Decision {
    
    // TODO find a way to refer to piece classes' icons? i18n?
    QUEEN(new Resource("ca.keal.sastrane.chess.icon", "queen_white.png"), "Promote to Queen", Queen::new),
    ROOK(new Resource("ca.keal.sastrane.chess.icon", "rook_white.png"), "Promote to Rook", Rook::new),
    BISHOP(new Resource("ca.keal.sastrane.chess.icon", "bishop_white.png"), "Promote to Bishop", Bishop::new),
    KNIGHT(new Resource("ca.keal.sastrane.chess.icon", "knight_white.png"), "Promote to Knight", Knight::new);
    
    @NonNull
    @Getter
    private final Resource icon;
    
    @NonNull
    @Getter
    private final String name;
    
    @NonNull
    private final Supplier<Piece> pieceSupplier;
    
    @Override
    public void onChoose(@NonNull Round round) {
        Square lastMovePos = round.getMoves().peekLast().getEndPos();
        // If there's nothing at lastMovePos, we'll get an NPE - but that should be impossible
        round.getBoard().set(lastMovePos, Pair.of(pieceSupplier.get(), round.getBoard().get(lastMovePos).getRight()));
    }
    
}