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
        if (searchResult.isEmpty()) {
            System.out.println("No songs found for query: " + query);
        } else {
            songsToBeViewed.addAll(searchResult);
        }
    }

    public void updateSong(Song updatedSong) throws Exception {
        songManager.updateSongs(updatedSong);
        for (int i = 0; i < songsToBeViewed.size(); i++) {
            if (songsToBeViewed.get(i).getId() == updatedSong.getId()) {
                songsToBeViewed.set(i, updatedSong);
                break;
            }
        }
    }

    public void addSong(Song newSong) throws Exception {
        Song s = songManager.addSong(newSong);
        songsToBeViewed.add(s);

    }

}
