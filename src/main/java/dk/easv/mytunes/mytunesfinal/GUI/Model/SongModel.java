package dk.easv.mytunes.mytunesfinal.GUI.Model;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SongModel {

    private ObservableList<Song> songsToBeViewed;
    private SongManager songManager;

    public SongModel() throws Exception {

        songManager = new SongManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(songManager.getAllSongs());
    }

    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    public void searchSong(String query) throws Exception {
        List<Song> searchResult = songManager.songSearch(query);
        songsToBeViewed.clear();
        songsToBeViewed.addAll(searchResult);
    }

    public void updateSong(Song updatedSong) throws Exception {
        // update song in DAL layer (through the layers)
        songManager.updateSongs(updatedSong);

        for (Song song : songsToBeViewed) {
            if (song.getId() == updatedSong.getId()) {
                song.setTitle(updatedSong.getTitle());
                song.setDuration(updatedSong.getDuration());
                song.setArtist(updatedSong.getArtist());
                song.setGenre(updatedSong.getGenre());
                song.setFilePath(updatedSong.getFilePath());
                break; // Exit the loop after finding the matching song
            }
        }
    }

    public void CreateSong(Song newSongs) throws Exception {
        Song s = songManager.CreateSong(newSongs);
        songsToBeViewed.add(s);

    }

}
