package dk.easv.mytunes.mytunesfinal.DAO.db;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.DAO.IPlaylistDataAccess;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;


//import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO_DB implements IPlaylistDataAccess {
    private DBConnector playlistdatabaseConnector;
    private MediaPlayer mediaPlayer;

    public PlaylistDAO_DB() throws IOException {
        playlistdatabaseConnector = new DBConnector();
    }


    @Override
    public List<Playlist> getAllPlaylists() throws Exception {
        List<Playlist> allPlaylists = new ArrayList<>();
        String sql = "SELECT \n" +
                "    p.PlaylistID, \n" +
                "    p.Name, \n" +
                "    COUNT(DISTINCT sop.SongID) AS SongsAmount, \n" +
                "    COALESCE(SUM(s.Duration), 0) AS SongsDuration\n" +
                "FROM Playlist p\n" +
                "LEFT JOIN SongsOnPlaylist sop ON p.PlaylistID = sop.PlaylistID\n" +
                "LEFT JOIN Song s ON sop.SongID = s.SongID\n" +
                "GROUP BY p.PlaylistID, p.Name;\n";


        try (Connection conn = playlistdatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            while (rs.next()) {
                int playlistID = rs.getInt("PlaylistID");
                String name = rs.getString("Name");
                int songsAmount = rs.getInt("SongsAmount");
                int songsDuration = rs.getInt("SongsDuration");

                Playlist playlist = new Playlist(playlistID, name, songsAmount, songsDuration);
                allPlaylists.add(playlist);

                System.out.println("SongsAmount: " + rs.getString("SongsAmount"));
                System.out.println("SongsDuration: " + rs.getString("SongsDuration"));


            }
        } catch (SQLException ex) {
            throw new Exception("Error retrieving all playlists: " + ex.getMessage(), ex);
        }
        return allPlaylists;
    }

    public int getSongCountForPlaylist(int playlistID) {
        // SQL query to count songs in a specific playlist
        String sql = "SELECT count(*) FROM dbo.SongsOnPlaylist WHERE PlaylistID = ?";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the playlistId parameter in the query
            pstmt.setInt(1, playlistID);

            try (ResultSet rs = pstmt.executeQuery()) {
                // If the result set is valid, return the count
                if (rs.next()) {
                    return rs.getInt(1); // The count is in the first column of the result set
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle this exception more appropriately in a real application
        }
        // Return 0 or throw an exception if there was an error or the playlist does not exist
        return 0;
    }

    public List<Song> getSongsForPlaylist(int playlistID) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.SongID, s.Title, s.Duration, s.FilePath, s.ArtistName, g.GenreName, sop.OrderIndex " +
                        "FROM Song s " +
                        "JOIN SongsOnPlaylist sop ON s.SongID = sop.SongID " +
                        "JOIN Genre g ON s.GenreID = g.GenreID " +
                        "WHERE sop.PlaylistID = ? ORDER BY sop.OrderIndex";


        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    int songID = rs.getInt("SongID");
                    String title = rs.getString("Title");
                    String artistName = rs.getString("ArtistName");
                    String genreName = rs.getString("GenreName");
                    int duration = rs.getInt("Duration");
                    String filePath = rs.getString("FilePath");


                    Song song = new Song(songID, title, artistName, genreName, duration, filePath);
                    songs.add(song);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return songs;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) throws Exception {

        // The SQL command
        String sqlP = "INSERT INTO Playlist (Name) VALUES (?);";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmtP = conn.prepareStatement(sqlP, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            // Bind the name parameter

            pstmtP.setString(1, playlist.getName());

            // Execute the insert operation
            int affectedRows = pstmtP.executeUpdate();

            // Check if the insert was successful
            if (affectedRows == 0) {
                throw new SQLException("Creating playlist failed, no rows affected.");
            }
            int newPlaylistID = -1;
            try (ResultSet generatedKeys = pstmtP.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Assuming your Playlist table has an auto-incremented ID
                    newPlaylistID = generatedKeys.getInt(1);

                } else {
                    throw new SQLException("Creating playlist failed, no ID obtained.");
                }
            }

            conn.commit();
            playlist.setId(newPlaylistID);
            return playlist;
        } catch (SQLException ex) {
            try (Connection conn = playlistdatabaseConnector.getConnection()) {
                conn.rollback();
            } catch (SQLException e) {
                ex.addSuppressed(e);
            }
            // Handle any SQL exceptions here
            throw new Exception("Error creating playlist: " + ex.getMessage(), ex);
        }

    }

    public void addSongToPlaylist(int playlistId, int songId) {
        String sql = "INSERT INTO SongsOnPlaylist (PlaylistID, SongID) VALUES (?, ?)";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding song to playlist: " + e.getMessage());
        }
    }

    public void deletePlaylist(Playlist playlist) throws Exception {
        String removeSongsSQL = "DELETE FROM SongsOnPlaylist WHERE PlaylistID = ?";
        String deletePlaylistSQL = "DELETE FROM Playlist WHERE PlaylistID = ?";

        try (Connection conn = playlistdatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement removeSongsStmt = conn.prepareStatement(removeSongsSQL);
                 PreparedStatement deletePlaylistStmt = conn.prepareStatement(deletePlaylistSQL)) {

                // Fjern alle sange fra playlisten
                removeSongsStmt.setInt(1, playlist.getId());
                removeSongsStmt.executeUpdate();

                // Slet selve playlisten
                deletePlaylistStmt.setInt(1, playlist.getId());
                deletePlaylistStmt.executeUpdate();

                conn.commit(); // Commit transaction
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction ved fejl
                throw new Exception("Error deleting playlist: " + e.getMessage(), e);
            }
        }
    }

    public void editPlaylist(Playlist playlist) throws Exception {
        String sql = "UPDATE Playlist SET Name = ? WHERE PlaylistID = ?";
        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playlist.getName());
            pstmt.setInt(2, playlist.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error updating playlist: " + ex.getMessage(), ex);
        }
    }

    public void removeSongFromPlaylist(int playlistId, int songId) throws Exception {
        String sql = "DELETE FROM SongsOnPlaylist WHERE PlaylistID = ? AND SongID = ?";
        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, songId);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error deleting song from playlist: " + ex.getMessage(), ex);
        }
    }

        public Song getSongById(int id) {
            String sql = "SELECT * FROM Song WHERE SongID = ?";
            try (Connection conn = playlistdatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Song(
                            rs.getInt("SongID"),
                            rs.getString("Title"),
                            rs.getString("ArtistName"),
                            rs.getString("GenreID"),
                            rs.getInt("Duration"),
                            rs.getString("FilePath")

                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null; // Return null if no song is found or an error occurs
        }

    public void updateSongOrder(int playlistId, List<Song> songs) throws SQLException {
        String sql = "UPDATE SongsOnPlaylist SET OrderIndex = ? WHERE PlaylistID = ? AND SongID = ?";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Disable auto-commit for batch execution

            for (Song song : songs) {
                pstmt.setInt(1, song.getOrderIndex());
                pstmt.setInt(2, playlistId);
                pstmt.setInt(3, song.getId());
                pstmt.addBatch(); // Add to batch
            }

            pstmt.executeBatch(); // Execute batch
            conn.commit(); // Commit the transaction
        } catch (SQLException e) {
            // Rollback or handle the exception as needed
            throw e;
        }
    }

        }

