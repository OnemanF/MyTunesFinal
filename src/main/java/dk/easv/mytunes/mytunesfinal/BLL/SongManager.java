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
        songDAO.updateSongs(selectedSong);
    }

    public Song addSong(Song newSong) throws Exception {

        Artist artist = new Artist(-1, newSong.getArtist());
        Genre genre = new Genre(-1, newSong.getGenre());
        artist.setArtistID(artistDAO_DB.getArtistID(newSong.getArtist()));
        genre.setGenreID(genreDAO_DB.getGenreID(newSong.getGenre()));
        if (artist.getArtistID()== -1) {
            artist = artistDAO_DB.insertArtist(new Artist(-1,newSong.getArtist()));

        }

        return songDAO.addSong(newSong,artist.getArtistID(),genre.getGenreID());
    }


}
