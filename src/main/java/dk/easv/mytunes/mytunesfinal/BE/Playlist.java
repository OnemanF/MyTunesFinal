package dk.easv.mytunes.mytunesfinal.BE;

public class Playlist {

    private int PlaylistID;
    private String name;
    private int playlistTotalDuration;
    private int SongsAmount;
    private Integer numberOfSongs;

    public Playlist(int PlaylistID, String name, int playlistTotalDuration, int SongsAmount) {
        this.PlaylistID = PlaylistID;
        this.name = name;
        this.playlistTotalDuration = playlistTotalDuration;
        this.SongsAmount = SongsAmount;
    }

    public int getId() {
        return PlaylistID;
    }

    public void setId(int PlaylistID) {
        this.PlaylistID = PlaylistID;
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

    public Integer getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSong(Integer numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public void setPlaylistTotalDuration(int playlistTotalDuration) {
        this.playlistTotalDuration = playlistTotalDuration;
    }

    public int getSongsAmount() {
        return SongsAmount;
    }

    public void setSongsAmount(int totalSongs) {
        this.SongsAmount = totalSongs;
    }





}
