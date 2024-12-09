package dk.easv.mytunes.mytunesfinal.GUI.Model;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.PlaylistManager;
import dk.easv.mytunes.mytunesfinal.BLL.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.util.List;

public class PlaylistModel {

    private ObservableList<Playlist> playlist;
    private PlaylistManager playlistManager;
    private ObservableList<Song> playlistSongs = FXCollections.observableArrayList();



    public PlaylistModel() throws Exception {

        playlistManager = new PlaylistManager();
        playlist = FXCollections.observableArrayList();
    }

    public ObservableList<Playlist> getPlaylists() {
        return playlist;
    }

    public void loadPlaylists() {
        List<Playlist> playlistData = null;
        try {
            playlistData = playlistManager.getAllPlaylists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Playlist playlist : playlistData) {
            int songCount = playlistManager.getSongCountForPlaylist(playlist.getId());
            playlist.setNumberOfSong(songCount);
        }
        playlist.setAll(playlistData);
    }

    public ObservableList<Song> getPlaylistSongs() {
        return playlistSongs;
    }

    public void loadSongsForPlaylist(int playlistId) {
        List<Song> songsListForPlaylist = null;
        try {
            songsListForPlaylist = playlistManager.getSongsForPlaylist(playlistId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        playlistSongs.clear();
        playlistSongs.addAll(songsListForPlaylist);
    }

    public void createPlaylist(String playlistName) {
        Playlist newPlaylist = null;

        // Check if the playlist name is valid before proceeding
        if (playlistName != null && !playlistName.trim().isEmpty()) {
            try {
                // Create the new playlist using playlistManager
                newPlaylist = playlistManager.createPlaylist(playlistName);

                // Check if newPlaylist is not null before adding it to the list
                if (newPlaylist != null) {
                    playlist.add(newPlaylist);
                } else {
                    System.out.println("Failed to create playlist.");
                }
            } catch (Exception e) {
                // Log or handle the exception properly
                System.err.println("Error creating playlist: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid playlist name.");
        }
        /*
        try {
            newPlaylist = playlistManager.createPlaylist(playlistName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        playlist.add(newPlaylist);

         */
    }

    public void addSongToPlaylist( int playlistId) throws Exception {
        // Delegate to the PlaylistManager
        playlistManager.addSongToPlaylist( playlistId);
    }
}
