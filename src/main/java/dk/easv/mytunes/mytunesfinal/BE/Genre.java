package dk.easv.mytunes.mytunesfinal.BE;

public class Genre {

    private String genreName;
    private int genreID;

    public Genre(int genreID, String genreName) {
        this.genreName = genreName;
        this.genreID = genreID;
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public int getGenreID() {
        return genreID;
    }

    public String getGenreName() {
        return genreName;
    }

    @Override
    public String toString() {
        return genreName;
    }
}
