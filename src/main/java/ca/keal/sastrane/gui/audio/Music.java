package ca.keal.sastrane.gui.audio;

import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.util.Utils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * Plays background music. Thanks, <a href="https://dzone.com/articles/javafx-2-gametutorial-part-5">Carl Dea</a>.
 */
public final class Music {
    
    @Nullable private static MediaPlayer player = null;
    @Getter private static double volume = 0.5;
    private static boolean shuffling = false;
    
    private Music() {
        throw new IllegalStateException("Music is a utility class and thus cannot be instantiated");
    }
    
    @SuppressWarnings("ConstantConditions")
    public static void setVolume(double volume) {
        if (isPlaying()) {
            player.setVolume(volume);
        }
        Music.volume = volume;
    }
    
    public static void play(Resource resource) {
        if (isPlaying()) {
            boolean oldShuffle = shuffling;
            stop();
            shuffling = oldShuffle;
        }
        player = new MediaPlayer(new Media(resource.getFullFilename()));
        player.setVolume(volume);
        player.play();
    }
    
    @SuppressWarnings("ConstantConditions")
    public static void repeat(Resource resource) {
        play(resource);
        player.setCycleCount(Integer.MAX_VALUE);
    }
    
    /**
     * Plays the indicated music files randomly, as if on shuffle, until stopped.
     * @param resources The list of available music files.
     */
    @SuppressWarnings("ConstantConditions")
    public static void shuffleAll(Resource... resources) {
        shuffling = true;
        Runnable pickNextMusic = () -> {
            if (/* everyday I'm */ shuffling) {
                play(Utils.randomChoice(resources));
            }
        };
        pickNextMusic.run();
        player.setOnEndOfMedia(pickNextMusic);
    }
    
    @SuppressWarnings("ConstantConditions")
    public static void stop() {
        if (!isPlaying()) throw new IllegalStateException("Cannot stop when not playing");
        shuffling = false;
        player.stop();
        player = null;
    }
    
    public static boolean isPlaying() {
        return player != null;
    }
    
}