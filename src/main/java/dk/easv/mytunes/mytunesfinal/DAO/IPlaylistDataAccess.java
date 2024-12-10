package dk.easv.mytunes.mytunesfinal.DAO;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;

import java.util.List;

public interface IPlaylistDataAccess {


    List<Playlist> getAllPlaylists() throws Exception;

    Playlist createPlaylist(Playlist playlist) throws Exception;

    void editPlaylist(Playlist playlist) throws Exception;
}
