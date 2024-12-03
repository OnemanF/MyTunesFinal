package dk.easv.mytunes.mytunesfinal.DAO.db;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.DAO.ISongDataAccess;

import java.io.IOException;
import java.sql.*;
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

            String sql = "SELECT * FROM song";


            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {


                int id = rs.getInt("SongID");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                int duration = rs.getInt("Duration");
                String filePath = rs.getString("FilePath");
                String genre = rs.getString("Genre");




                Song song = new Song( id,  title, artist,genre, duration,filePath);
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
    public void updateSongs(Song song) throws Exception {

    }

    @Override
    public void deleteSong(Song song) throws Exception {

    }

    @Override
    public Song createSongs(Song song) throws Exception {

        // SQL command
        String sql = "INSERT INTO Songs (Title, Duration, Artist, Genre, FilePath) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = SongdatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setLong(2, song.getDuration());
            //stmt.setInt(3, song.artist);
            //stmt.setInt(4, genreId);
            stmt.setString(5, song.getFilePath());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            Song createdSong = new Song(id, song.getArtist(),song.getTitle(),song.getGenre() , song.getDuration() , song.getFilePath());

            return createdSong;
        } catch (SQLException ex) {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not insert song", ex);
        }
    }


}
