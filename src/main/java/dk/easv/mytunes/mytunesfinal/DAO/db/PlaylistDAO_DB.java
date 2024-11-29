package dk.easv.mytunes.mytunesfinal.DAO.db;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
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
        String sql = "Select * from playlist";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {



            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                int songsDuration = rs.getInt("SongsDuration");
                int Songs = rs.getInt("Songs");
                Playlist playlist = new Playlist(id, name, Songs, songsDuration );
                allPlaylists.add(playlist);


            }
        } catch (SQLException ex) {
            throw new Exception("Error retrieving all playlists: " + ex.getMessage(), ex);
        }
        return allPlaylists;
    }

    public int getSongCountForPlaylist(int playlistId) {
        // SQL query to count songs in a specific playlist
        String sql = "SELECT COUNT(*) FROM dbo.PlaylistSongs WHERE PlaylistId = ?";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the playlistId parameter in the query
            pstmt.setInt(1, playlistId);

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

}