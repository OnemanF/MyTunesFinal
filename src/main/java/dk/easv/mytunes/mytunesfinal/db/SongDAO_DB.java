package dk.easv.mytunes.mytunesfinal.db;

import dk.easv.mytunes.mytunesfinal.BE.Song;

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

            String sql = "SELECT * " +
                    "FROM Songs ";
                   // "JOIN Genre ON Songs.GenreID = Genre.GenreID " +
                  //  "JOIN Artist ON Songs.ArtistID = Artist.ArtistID;"

            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {


                int id = rs.getInt("SongID");
                String artist = rs.getString("ArtistName");
                String title = rs.getString("SongTitle");
                String genre = rs.getString("GenreType");
                int duration = rs.getInt("SongDuration");
                String FilePath = rs.getString("FilePath");


                Song song = new Song(id, artist, title, genre, duration, FilePath);
                allSongs.add(song);
            }
            return allSongs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database", ex);
        }
    }
}
