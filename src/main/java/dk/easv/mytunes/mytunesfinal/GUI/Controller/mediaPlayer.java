
package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
//import javax.print.attribute.standard.Media;
import java.io.File;

public class MediaPlayer {

    private javafx.scene.media.MediaPlayer mediaPlayer;
    private String folder = "music\\";
    private String currentSongFilePath = "";
    private boolean isPlaying = false;
    private SongEndListener songEndListener;
    private DoubleProperty currentSongPosition = new SimpleDoubleProperty(0.0);
    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty(0.0);

    // Functional interface for callback
    public interface SongEndListener {
        void onSongEnd();
    }

    // Plays media from a specified file path.
    public void playMedia(String fileName) {
        File mediaFile = new File(folder + fileName);

        if (mediaFile.exists()) {
            // Play the paused song from where it was paused
            if (mediaPlayer != null && fileName.equals(currentSongFilePath) && !isPlaying) {
                mediaPlayer.play();
                isPlaying = true;
                setPlayerTimeListener();
            } else {
                // Stops the currently playing song inorder to start a new
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }

                // play new song
                Media media = new Media(mediaFile.toURI().toString());
                mediaPlayer = new javafx.scene.media.MediaPlayer(media);


                mediaPlayer.setOnEndOfMedia(() -> {
                    if (songEndListener != null) {
                        songEndListener.onSongEnd();
                    }
                });

                // sets the volume to the same value as before a new song was selected
                //mediaPlayer.setVolume(currentVolume);

                mediaPlayer.play();
                isPlaying = true;
                currentSongFilePath = fileName;
                setPlayerTimeListener();
            }
        }
    }

    // Returns the total duration of the media in seconds.
    public double getTotalDurationInSeconds() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0;
    }

    // Sets a listener for tracking the current time of the media
    private void setPlayerTimeListener() {
        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((obs, ov, nv) -> {
                double curPos = nv.toSeconds();
                double totalDur = getTotalDurationInSeconds();
                double scaledPos = (curPos / totalDur) * 100.0;
                currentSongPosition.set(scaledPos);
                currentTimeProperty.set(nv.toSeconds());
            });
        }
    }

    public String getCurrentSongFilePath() {
        return currentSongFilePath;
    }
}
