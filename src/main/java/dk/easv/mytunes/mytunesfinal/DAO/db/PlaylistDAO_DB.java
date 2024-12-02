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
        String sql = " SELECT s.SongID, s.Title, s.Duration, s.FilePath " +
                "FROM Songs s " +

                "INNER JOIN SongsOnPlaylist sop ON s.SongID = sop.SongID " +
                "WHERE sop.PlaylistID = ?";

        try (Connection conn = playlistdatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("SongID");
                    String title = rs.getString("Title");
                    int duration = rs.getInt("Duration");
                    String filePath = rs.getString("FilePath");
                    String name = rs.getString("Artist");
                    String genre = rs.getString("Genre");

                    Song song = new Song(id, name, title, genre, duration, filePath);
                    songs.add(song);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return songs;
    }




}