package dk.easv.mytunes.mytunesfinal.BE;

public class Song {

    private String genre;
    private int duration;
    private int id;
    private String title;
    private String artist;
    private String FilePath;


    public Song(int duration, String filePath, String artist, String title, int id, String genre) {
        FilePath = filePath;
        this.artist = artist;
        this.title = title;
        this.id = id;
        this.duration = duration;
        this.genre = genre;
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
