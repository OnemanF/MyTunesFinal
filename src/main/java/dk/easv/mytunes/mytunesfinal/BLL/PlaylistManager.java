package dk.easv.mytunes.mytunesfinal.BLL;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.DAO.IPlaylistDataAccess;
import dk.easv.mytunes.mytunesfinal.DAO.db.PlaylistDAO_DB;

import java.io.IOException;
import java.util.List;

public class PlaylistManager {

    private IPlaylistDataAccess playlistDAO;

    private PlaylistDAO_DB playlistDAO_DB;

    public PlaylistManager() throws IOException {
        playlistDAO = new PlaylistDAO_DB();
        this.playlistDAO_DB = new PlaylistDAO_DB();
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.getAllPlaylists();
    }




    public int getSongCountForPlaylist(int playlistId) {
        return playlistDAO_DB.getSongCountForPlaylist(playlistId);
    }
}