package dk.easv.mytunes.mytunesfinal.BLL.util;

import dk.easv.mytunes.mytunesfinal.BE.Song;

import java.util.ArrayList;
import java.util.List;

public class Search {

    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();

        for (Song song : searchBase) {
            if(compareToSongTitle(query, song) || compareToSongGenre(query, song))
            {
                searchResult.add(song);
            }
        }

        return searchResult;
    }

    private boolean compareToSongGenre(String query, Song song) {
        return song.getGenre().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongTitle(String query, Song song) {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }

}
