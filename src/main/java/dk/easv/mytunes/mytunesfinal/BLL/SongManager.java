package dk.easv.mytunes.mytunesfinal.BLL;

import dk.easv.mytunes.mytunesfinal.BE.Genre;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.util.Search;
import dk.easv.mytunes.mytunesfinal.DAO.ISongDataAccess;
import dk.easv.mytunes.mytunesfinal.DAO.db.GenreDAO_DB;
import dk.easv.mytunes.mytunesfinal.DAO.db.SongDAO_DB;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private Search songSearch = new Search();
    private ISongDataAccess songDAO;
    private GenreDAO_DB genreDAO_DB;

    public SongManager() throws IOException {
        songDAO = new SongDAO_DB();
        genreDAO_DB = new GenreDAO_DB();

    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();

    }

    public List<Song> songSearch(String query) throws Exception {
        List<Song> allSongs = getAllSongs();
        List<Song> searchResult = songSearch.search(allSongs, query);
        return searchResult;

    }

    public void updateSongs(Song selectedSong) throws Exception {

        try {
            String artistName = selectedSong.getArtist();
            int genreID = ensureGenreExists(selectedSong.getGenre());

            // Pass the updated song along with artist and genre IDs to the DAO
            songDAO.updateSongs(selectedSong, artistName, genreID);

        } catch (Exception e) {
            // Log and rethrow for higher-level handling
            System.err.println("Error updating song: " + e.getMessage());
            throw e;
        }

    }

    private int ensureGenreExists(String genreName) throws Exception {
        int genreID = genreDAO_DB.getGenreID(genreName);
        if (genreID == -1) {
            Genre newGenre = genreDAO_DB.insertGenre(new Genre(-1, genreName));
            genreID = newGenre.getGenreID();
        }
        return genreID;
    }


    public Song addSong(Song newSong) throws Exception {
        try {
            String artistName = newSong.getArtist();
            int genreID = ensureGenreExists(newSong.getGenre());

            // Add the song via DAO and return the result
            return songDAO.addSong(newSong, artistName, genreID);

        } catch (Exception e) {
            // Log and rethrow for higher-level handling
            System.err.println("Error adding song: " + e.getMessage());
            throw e;
        }
    }

    public void deleteSong(Song song) throws Exception {
        songDAO.deleteSong(song);
    }
    }



