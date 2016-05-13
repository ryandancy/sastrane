package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.move.MovingMove;

public final class XiangqiUtils {
    
    /** river is between ranks 5 and 6 */
    private static final int RIVER_Y = 5;
    
    private XiangqiUtils() {
        throw new IllegalStateException("XiangqiUtils is a utility class and therefore cannot be constructed.");
    }
    
    public static boolean doesMoveCrossRiver(MovingMove move) {
        return (move.getFrom().getY() > RIVER_Y) != (move.getTo().getY() > RIVER_Y);
    }
    
}