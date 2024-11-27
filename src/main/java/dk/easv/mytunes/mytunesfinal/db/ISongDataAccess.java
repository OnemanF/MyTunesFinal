package dk.easv.mytunes.mytunesfinal.db;



import dk.easv.mytunes.mytunesfinal.BE.Song;

import java.util.List;

public interface ISongDataAccess {

    List<Song> getAllSongs() throws Exception;
}
