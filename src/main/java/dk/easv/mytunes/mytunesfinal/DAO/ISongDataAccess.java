package dk.easv.mytunes.mytunesfinal.DAO;



import dk.easv.mytunes.mytunesfinal.BE.Song;

import java.util.List;

public interface ISongDataAccess {

    List<Song> getAllSongs() throws Exception;
    void updateSongs(Song song, int artistID, int genreID) throws Exception;
    void deleteSong(Song song) throws Exception;


    Song addSong(Song newSong, int artistID, int genreID) throws Exception;
}
