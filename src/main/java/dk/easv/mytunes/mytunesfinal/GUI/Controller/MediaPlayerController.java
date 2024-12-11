package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.DAO.db.PlaylistDAO_DB;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;

public class MediaPlayerController {
    private MediaPlayer mediaPlayer;
    private PlaylistDAO_DB playlistDAO;
    private String folder = "music\\";

    public MediaPlayerController(PlaylistDAO_DB playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public void playTheSong(int id) {
        Song song = playlistDAO.getSongById(id); // Fetch the song by ID
        if (song != null) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop(); // Stop the current song
                }

                String fullPath = folder + song.getFilePath();
                File file = new File(fullPath);

                if (!file.exists()) {
                    System.out.println("File not found: " + fullPath);
                    return; // Exit if the file does not exist
                }

                // Create a new Media object with the song's file path
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

                System.out.println("Now playing: " + song.getTitle());
            } catch (Exception e) {
                System.out.println("Error playing song: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Song with ID " + id + " not found.");
        }
    }
}


