package dk.easv.mytunes.mytunesfinal.BLL;

import dk.easv.mytunes.mytunesfinal.BE.Artist;
import dk.easv.mytunes.mytunesfinal.BE.Genre;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.util.Search;
import dk.easv.mytunes.mytunesfinal.DAO.ISongDataAccess;
import dk.easv.mytunes.mytunesfinal.DAO.db.ArtistDAO_DB;
import dk.easv.mytunes.mytunesfinal.DAO.db.GenreDAO_DB;
import dk.easv.mytunes.mytunesfinal.DAO.db.SongDAO_DB;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private Search songSearch = new Search();
    private ISongDataAccess songDAO;
    private ArtistDAO_DB artistDAO_DB;
    private GenreDAO_DB genreDAO_DB;

    public SongManager() throws IOException {
        songDAO = new SongDAO_DB();
        genreDAO_DB = new GenreDAO_DB();
        artistDAO_DB = new ArtistDAO_DB();
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

        /*
        Artist artist = new Artist(-1, selectedSong.getArtist());
        Genre genre = new Genre(-1, selectedSong.getGenre());
        artist.setArtistID(artistDAO_DB.getArtistID(selectedSong.getArtist()));
        genre.setGenreID(genreDAO_DB.getGenreID(selectedSong.getGenre()));
        if (artist.getArtistID()== -1) {
            artist = artistDAO_DB.insertArtist(new Artist(-1,selectedSong.getArtist()));

        }
        songDAO.updateSongs(selectedSong, artist.getArtistID(),genre.getGenreID());

         */
    }
    private int ensureArtistExists(String artistName) throws Exception {
        int artistID = artistDAO_DB.getArtistID(artistName);
        if (artistID == -1) {
            Artist newArtist = artistDAO_DB.insertArtist(new Artist(-1, artistName));
            artistID = newArtist.getArtistID();
        }
        return artistID;
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
            int artistID = ensureArtistExists(newSong.getArtist());
            int genreID = ensureGenreExists(newSong.getGenre());

            // Add the song via DAO and return the result
            return songDAO.addSong(newSong, artistID, genreID);

        } catch (Exception e) {
            // Log and rethrow for higher-level handling
            System.err.println("Error adding song: " + e.getMessage());
            throw e;
        }
    }
        /*
        Artist artist = new Artist(-1, newSong.getArtist());
        Genre genre = new Genre(-1, newSong.getGenre());
        artist.setArtistID(artistDAO_DB.getArtistID(newSong.getArtist()));
        genre.setGenreID(genreDAO_DB.getGenreID(newSong.getGenre()));
        if (artist.getArtistID()== -1) {
            artist = artistDAO_DB.insertArtist(new Artist(-1,newSong.getArtist()));

        }

        return songDAO.addSong(newSong,artist.getArtistID(),genre.getGenreID());

         */
    }



