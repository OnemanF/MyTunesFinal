package dk.easv.mytunes.mytunesfinal.DAO.db;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.DAO.IPlaylistDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO_DB implements IPlaylistDataAccess {
    private DBConnector playlistdatabaseConnector;

    public PlaylistDAO_DB() throws IOException {
        playlistdatabaseConnector = new DBConnector();
    }


    @Override
    public List<Playlist> getAllPlaylists() throws Exception {
        List<Playlist> allPlaylists = new ArrayList<>();
        String sql = "SELECT playlist.PlaylistID, playlist.Name, COUNT(sop.SongID) as SongsAmount, coalesce(SUM(Song.Duration),0) as SongsDuration\n" +
                "From Playlist playlist\n" +
                "Left JOIN SongsOnPlaylist sop ON playlist.PlaylistID = sop.PlaylistID\n" +
                "Left JOIN Song song ON sop.SongID = song.SongID\n" +
                "GROUP BY playlist.PlaylistID, playlist.Name";


        try (Connection conn = playlistdatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {



            while (rs.next()) {
                int playlistID = rs.getInt("PlaylistID");
                String name = rs.getString("Name");
                int songsDuration = rs.getInt("SongsDuration");
                int songsAmount = rs.getInt("SongsAmount");
                Playlist playlist = new Playlist(playlistID, name, songsAmount, songsDuration );
                allPlaylists.add(playlist);


            }
        } catch (SQLException ex) {
            throw new Exception("Error retrieving all playlists: " + ex.getMessage(), ex);
        }
        return allPlaylists;
    }

    public int getSongCountForPlaylist(int playlistID) {
        // SQL query to count songs in a specific playlist
        String sql = "SELECT COUNT(*) FROM dbo.SongsOnPlaylist WHERE PlaylistID = ?";

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
        String sql = /*" SELECT s.SongID, s.Title, s.Duration, s.FilePath " +
                "FROM Songs s " +

                "INNER JOIN SongsOnPlaylist sop ON s.SongID = sop.SongID " +
                "WHERE sop.PlaylistID = ?";*/

        "SELECT s.SongID, s.Title, s.Duration, s.FilePath, s.ArtistName, s.GenreID " +
                "FROM Song s " +
                "INNER JOIN SongsOnPlaylist sop ON s.SongID = sop.SongID " +
                "WHERE sop.PlaylistID = ?";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    int id = rs.getInt("SongID");
                    String title = rs.getString("Title");
                    String artistName = rs.getString("ArtistName");
                    String genreName = rs.getString("GenreName");
                    int duration = rs.getInt("Duration");
                    String filePath = rs.getString("FilePath");



                    Song song = new Song(id, title, artistName, genreName, duration, filePath);
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
                    newPlaylistID  = generatedKeys.getInt(1);

                } else {
                    throw new SQLException("Creating playlist failed, no ID obtained.");
                }
            }

            conn.commit();
            playlist.setId(newPlaylistID);
            return playlist;
        } catch (SQLException ex) {
            try(Connection conn = playlistdatabaseConnector.getConnection()) {
                conn.rollback();
            }catch (SQLException e){
                ex.addSuppressed(e);
            }
            // Handle any SQL exceptions here
            throw new Exception("Error creating playlist: " + ex.getMessage(), ex);
        }

    }

    public void addSongToPlaylist( int playlistId) {
        String sql = "INSERT INTO Playlist (PlaylistID) VALUES (?)";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding song to playlist: " + e.getMessage());
        }
    }


}