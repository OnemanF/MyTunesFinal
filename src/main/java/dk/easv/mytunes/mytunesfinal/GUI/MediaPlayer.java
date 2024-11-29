package dk.easv.mytunes.mytunesfinal.GUI;

import javafx.scene.media.Media;

import java.io.File;

public class MediaPlayer {

    private javafx.scene.media.MediaPlayer mediaPlayer;
    private String folder = "music\\";
    private boolean isPlaying = false;


    // Play media from filepath
    public void playMedia(String fileName) {
        File songFile = new File(folder + fileName);

        // play new song
        Media media = new Media(songFile.toURI().toString());
        mediaPlayer = new javafx.scene.media.MediaPlayer(media);

        mediaPlayer.play();
    }


}
