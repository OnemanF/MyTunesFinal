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

    public void loadInPlaylists() {
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
        try {
            newPlaylist = playlistManager.createPlaylist(playlistName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        playlist.add(newPlaylist);
    }










}
