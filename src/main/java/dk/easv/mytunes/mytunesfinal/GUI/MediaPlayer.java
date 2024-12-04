<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
package dk.easv.mytunes.mytunesfinal.GUI;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableView;
import javafx.scene.media.Media;
import javafx.util.Duration;

=======

package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
//import javax.print.attribute.standard.Media;
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java
import java.io.File;

public class MediaPlayer {

    private javafx.scene.media.MediaPlayer mediaPlayer;
    private String folder = "music\\";
<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
    private double currentVolume = 0.5;
    private boolean isPlaying = false;
    private String currentSongFilePath = "";
    private DoubleProperty currentSongPosition = new SimpleDoubleProperty(0.0);
    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty(0.0);
    private SongEndListener songEndListener;
    private TableView<Song> lastInteractedTableView;
=======
    private String currentSongFilePath = "";
    private boolean isPlaying = false;
    private SongEndListener songEndListener;
    private DoubleProperty currentSongPosition = new SimpleDoubleProperty(0.0);
    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty(0.0);
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java

    // Functional interface for callback
    public interface SongEndListener {
        void onSongEnd();
    }

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
    // Constructor
    public MediaPlayer(){
        setVolume(0.5);
        setVolume(currentVolume);// Sets previous volume

    }

    // Play media from filepath
    public void playMedia(String fileName) {
        File songFile = new File(folder + fileName);

        if (songFile.exists()) {
=======
    // Plays media from a specified file path.
    public void playMedia(String fileName) {
        File mediaFile = new File(folder + fileName);

        if (mediaFile.exists()) {
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java
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
<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
                Media media = new Media(songFile.toURI().toString());
=======
                Media media = new Media(mediaFile.toURI().toString());
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java
                mediaPlayer = new javafx.scene.media.MediaPlayer(media);


                mediaPlayer.setOnEndOfMedia(() -> {
                    if (songEndListener != null) {
                        songEndListener.onSongEnd();
                    }
                });

                // sets the volume to the same value as before a new song was selected
<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
                mediaPlayer.setVolume(currentVolume);
=======
                //mediaPlayer.setVolume(currentVolume);
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java

                mediaPlayer.play();
                isPlaying = true;
                currentSongFilePath = fileName;
                setPlayerTimeListener();
            }
        }
    }

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
    // Registers a callback to be invoked when a song ends.
    public void setOnSongEndListener(SongEndListener songEndListener) {
        this.songEndListener = songEndListener;
    }

    // Pauses the currently playing media.
    public void pauseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    // Sets the volume for the media player.
    public void setVolume(double volume) {
        this.currentVolume = volume; // Update the current volume field
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume); // Set the volume of the mediaPlayer
        }
    }

    // Returns the current time of the media in a formatted string.
    public String getCurrentTimeFormatted() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            return formatTime(currentTime);
        }
        return "00:00";
    }

    // Returns the total duration of the media in a formatted string.
    public String getTotalDurationFormatted() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            Duration totalDuration = mediaPlayer.getTotalDuration();
            return formatTime(totalDuration);
        }
        return "00:00";
    }

    // Helper method to format time duration.
    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Checks if the media is currently playing.
    public boolean isMediaPlaying() {
        return mediaPlayer != null && isPlaying;
    }


    // Checks if the media is paused.
    public boolean isPaused() {
        return mediaPlayer != null && !isPlaying;
    }

    // Checks if the media is playing.
    public boolean isPlaying() {
        return isPlaying;
    }

    // Seeks to a specific time in the media.
    public void seekTo(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
            playMedia(currentSongFilePath);
        }
    }

=======
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java
    // Returns the total duration of the media in seconds.
    public double getTotalDurationInSeconds() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0;
    }

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java
    // Provides a property for binding the current song position.
    public DoubleProperty currentSongPositionProperty() {
        return currentSongPosition;
    }

=======
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java
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
<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MediaPlayer.java

=======
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MediaPlayer.java
