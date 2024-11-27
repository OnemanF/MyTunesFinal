package dk.easv.mytunes.mytunesfinal.BE;

public class Playlist {

    private int id;
    private String name;
    private int playlistTotalDuration;
    private int totalSongs;

    public Playlist(int id, String name, int playlistTotalDuration, int totalSongs) {
        this.id = id;
        this.name = name;
        this.playlistTotalDuration = playlistTotalDuration;
        this.totalSongs = totalSongs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaylistTotalDuration() {
        return playlistTotalDuration;
    }

    public void setPlaylistTotalDuration(int playlistTotalDuration) {
        this.playlistTotalDuration = playlistTotalDuration;
    }

    public int getTotalSongs() {
        return totalSongs;
    }

    public void setTotalSongs(int totalSongs) {
        this.totalSongs = totalSongs;
    }





}
