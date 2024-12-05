package dk.easv.mytunes.mytunesfinal.BE;

public class Song {

    private String genre;
    private int duration;
    private int id;
    private String title;
    private String artist;
    private String FilePath;


    public Song(int id, String title, String artist, String genre, int duration, String filePath) {

        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.FilePath = filePath;
    }

    public Song(String title, String artist, String genre, int duration, String filePath) {

        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.FilePath = filePath;
    }

        public String getFilePath () {
            return FilePath;
        }

        public void setFilePath (String filePath){
            FilePath = filePath;
        }

        public String getArtist () {
            return artist;
        }

        public void setArtist (String artist){
            this.artist = artist;
        }

        public String getTitle () {
            return title;
        }

        public void setTitle (String title){
            this.title = title;
        }

        public int getId () {
            return id;
        }

        public void setId ( int id){
            this.id = id;
        }

        public int getDuration () {
            return duration;
        }

        public void setDuration ( int duration){
            this.duration = duration;
        }

        public String getGenre () {
            return genre;
        }

        public void setGenre (String genre){
            this.genre = genre;
        }

    @Override
    public String toString()
    {
        return id + ": " + title + genre + duration + artist;
    }

}
