package ca.keal.sastrane.gui.audio;

import ca.keal.sastrane.util.Resource;
import javafx.scene.media.AudioClip;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Acts as a cache for sound effects and allows up to two effects to be played at the same time. Thanks, <a
 * href="https://dzone.com/articles/javafx-2-gametutorial-part-5">Carl Dea"</a>.
 */
public final class SoundEffects {
    
    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);
    private static final Map<Resource, AudioClip> CACHE = new HashMap<>();
    @Getter @Setter private static double volume = .5;
    
    private SoundEffects() {
        throw new IllegalStateException("SoundEffects is a utility class and thus cannot be instantiated");
    }
    
    public static void load(Resource resource) {
        if (!CACHE.containsKey(resource)) {
            CACHE.put(resource, new AudioClip(resource.getFilename()));
        }
    }
    
    public static void play(Resource resource) {
        POOL.execute(() -> CACHE.get(resource).play(volume));
    }
    
    public static void stop() {
        POOL.shutdown();
    }
    
}