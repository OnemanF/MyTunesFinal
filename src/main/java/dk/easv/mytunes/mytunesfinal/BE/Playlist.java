package dk.easv.mytunes.mytunesfinal.BE;

public class Playlist {

    private int PlaylistID;
    private String name;
    private int SongsAmount;
    private int playlistTotalDuration;

    private Integer numberOfSongs;

    public Playlist(int PlaylistID, String name, int SongsAmount, int playlistTotalDuration ) {
        this.PlaylistID = PlaylistID;
        this.name = name;
        this.SongsAmount = SongsAmount;
        this.playlistTotalDuration = playlistTotalDuration;

    }

    public Playlist(int PlaylistID, String name) {
        this.PlaylistID = PlaylistID;
        this.name = name;
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
