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

            String sql = "SELECT s.SongID, s.Title, s.Duration, s.FilePath, s.ArtistName, g.GenreName " +
                    "FROM Song s " +
                    "JOIN Genre g ON s.GenreID = g.GenreID";


            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {


                int id = rs.getInt("SongID");
                String title = rs.getString("Title");
                String artistName = rs.getString("ArtistName");
                String genreName = rs.getString("GenreName");
                int duration = rs.getInt("Duration");
                String filePath = rs.getString("FilePath");


                Song song = new Song( id,  title, artistName, genreName, duration, filePath);
                allSongs.add(song);
            }
            return allSongs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get music from database", ex);
        }
    }


    @Override
    public void updateSongs(Song song, String artistName, int genreID) throws Exception {
        // SQL command
        String sql = "UPDATE dbo.Song SET Title = ?, Duration = ?, ArtistName = ?, GenreID = ?, FilePath = ? WHERE SongID = ?";

        System.out.println("Updating song with ID: " + song.getId());
        System.out.println("ArtistName: " + artistName);
        System.out.println("GenreID: " + genreID);


        try (Connection conn = SongdatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getDuration());
            stmt.setString(3, artistName);
            stmt.setInt(4, genreID);
            stmt.setString(5, song.getFilePath());
            stmt.setInt(6, song.getId());

            stmt.executeUpdate();




            // Run the specified SQL statement
        } catch (SQLException ex) {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
    }



    @Override
    public void deleteSong(Song song) throws Exception {
    String sql = "DELETE FROM Song WHERE SongID = ?";

    try (Connection conn = SongdatabaseConnector.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, song.getId());
        pstmt.executeUpdate(); //udfører sletning
    } catch (SQLException ex) {
        ex.printStackTrace(); //Udskriv fejlbeskeden til console
        throw new RuntimeException("Could not delete song:" + ex.getMessage(), ex);
    }
    }




    @Override
    public Song addSong(Song song, String artistName, int genreID) throws Exception {

        // SQL command
        String sql = "INSERT INTO dbo.Song (Title, Duration, ArtistName, GenreID, FilePath) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = SongdatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setLong(2, song.getDuration());
            stmt.setString(3, artistName);
            stmt.setInt(4, genreID);
            stmt.setString(5, song.getFilePath());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            Song createdSong = new Song(id, song.getArtist(), song.getTitle(), song.getGenre() , song.getDuration() , song.getFilePath());

            return createdSong;
        } catch (SQLException ex) {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not insert song", ex);
        }
    }


}
