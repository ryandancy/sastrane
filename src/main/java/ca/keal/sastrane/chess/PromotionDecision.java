package ca.keal.sastrane.chess;

import ca.keal.sastrane.Decision;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum PromotionDecision implements Decision {
    
    // TODO icons for all of these - find a way to refer to piece classes' icons? i18n?
    QUEEN(Pair.of("ca.keal.sastrane.chess.icon", "TODO_queen"), "Promote to Queen", Queen::new),
    ROOK(Pair.of("ca.keal.sastrane.chess.icon", "TODO_rook"), "Promote to Rook", Rook::new),
    BISHOP(Pair.of("ca.keal.sastrane.chess.icon", "TODO_bishop"), "Promote to Bishop", Bishop::new),
    KNIGHT(Pair.of("ca.keal.sastrane.chess.icon", "TODO_knight"), "Promote to Knight", Knight::new);
    
    @NonNull
    @Getter
    private final Pair<String, String> packageAndIcon;
    
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