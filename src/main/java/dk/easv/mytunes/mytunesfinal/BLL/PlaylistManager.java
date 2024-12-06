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

    public List<Song> getSongsForPlaylist(int playlistId) throws Exception {
        return playlistDAO_DB.getSongsForPlaylist(playlistId);
    }

    public Playlist createPlaylist(String name) throws Exception {
        Playlist newPlaylist = new Playlist(-1,name,0,0);
        return playlistDAO.createPlaylist(newPlaylist);
    }

    public void addSongToPlaylist(int songId, int playlistId) throws Exception {
        // Delegate to the DAO layer
        playlistDAO_DB.addSongToPlaylist(songId, playlistId);
    }

}
