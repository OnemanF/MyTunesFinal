package dk.easv.mytunes.mytunesfinal.DAO;



import dk.easv.mytunes.mytunesfinal.BE.Song;

import java.util.List;

public interface ISongDataAccess {

    List<Song> getAllSongs() throws Exception;
    Song addSong(Song newSong) throws Exception;
    void editSong(Song song) throws Exception;
    void deleteSong(Song song) throws Exception;
}
