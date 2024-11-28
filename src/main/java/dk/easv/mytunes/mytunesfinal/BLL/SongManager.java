package dk.easv.mytunes.mytunesfinal.BLL;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.util.Search;
import dk.easv.mytunes.mytunesfinal.DAO.ISongDataAccess;
import dk.easv.mytunes.mytunesfinal.DAO.db.SongDAO_DB;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private Search songSearch = new Search();
    private ISongDataAccess songDAO;

    public SongManager() throws IOException {
        songDAO = new SongDAO_DB();
    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();

    }

    public List<Song> songSearch(String query) throws Exception {
        List<Song> allSongs = getAllSongs();
        List<Song> searchResult = songSearch.search(allSongs, query);
        return searchResult;

    }
}
