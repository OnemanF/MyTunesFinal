package dk.easv.mytunes.mytunesfinal.BE;

public class Song {

    private String genre;
    private int duration;
    private int id;
    private String title;
    private String artist;
    private String filePath;

    // Constructor with ID
    public Song(int id, String title, String artist, String genre, int duration, String filePath) {
        this.id = id;
        this.title = title != null ? title : "";
        this.artist = artist != null ? artist : "";
        this.genre = genre != null ? genre : "";
        this.duration = duration;
        this.filePath = filePath != null ? filePath : "";
    }

    // Constructor without ID
    public Song(String title, String artist, String genre, int duration, String filePath) {
        this(0, title, artist, genre, duration, filePath); // Call the other constructor
    }

    // Getters and Setters
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath != null ? filePath : "";
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist != null ? artist : "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre != null ? genre : "";
    }

    @Override
    public String toString() {
        return String.format("%d: %s | %s | %d | %s | %s",
                id, title, genre, duration, artist, filePath);
    }
}
