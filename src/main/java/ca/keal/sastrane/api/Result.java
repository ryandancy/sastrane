package ca.keal.sastrane.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class Result {
    
    public static final Result DRAW = new Result() {};
    public static final Result NOT_OVER = new Result() {};
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Win extends Result {
        private final Player player;
    }
    
}