package dk.easv.mytunes.mytunesfinal.DAO.db;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.DAO.ISongDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDAO_DB implements ISongDataAccess {

    private DBConnector SongdatabaseConnector;

    public SongDAO_DB() throws IOException {
        SongdatabaseConnector = new DBConnector();
    }

    @Override
    public List<Song> getAllSongs() throws Exception {
        ArrayList<Song> allSongs = new ArrayList<>();

        try (Connection conn = SongdatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Song";
                    //"JOIN Genre ON Song.Genre = Genre.Genre " +
                    //"JOIN Artist ON Song.Artist = Artist.Artist;";


            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {


                int id = rs.getInt("ID");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                //int duration = rs.getInt("Duration");
                String FilePath = rs.getString("FilePath");
                String genre = rs.getString("Genre");




                Song song = new Song(id,  title, artist, FilePath, genre);
                allSongs.add(song);
            }
            return allSongs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get music from database", ex);
        }
    }

    @Override
    public Song addSong(Song newSong) throws Exception {
        return null;
    }

    @Override
    public void editSong(Song song) throws Exception {

    }

    @Override
    public void deleteSong(Song song) throws Exception {

    }
}