package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.PlaylistManager;
import dk.easv.mytunes.mytunesfinal.GUI.Model.PlaylistModel;
import dk.easv.mytunes.mytunesfinal.GUI.Model.SongModel;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {
    //Table view
    @FXML
    private TableView<Song> tblSongsOnPlaylist, tblSongs;

    @FXML
    private TableView<Playlist> tblPlaylist;
    //Table columns
    @FXML
    private TableColumn<Song, String> colTitle, colArtist, colGenre, colDuration;

    @FXML
    private TableColumn<Song, String> colSong, colSongsArtist;

    //playlist table
    @FXML
    private TableColumn<Playlist, String> colName, colSongs, colSongsDuration;

    //search field
    @FXML
    private TextField txtSongSearch;

    @FXML
    private Label crntTrackTxt;

    //buttons
    @FXML
    private Button searchButton, btnNewPlaylist;

    @FXML
    private Button btnAddNewSong, btUpdateSong;

    @FXML
    private AddUpdateSong dialogboxes = new AddUpdateSong();

    private SongModel songModel;
    private PlaylistModel playlistModel;

    private MediaPlayer mediaPlayer;
    private int currentSongIndex = 0; // Track the current song being played.
    private List<Song> songPaths; // List of song file paths.
    private String folder = "music\\";

    private PlaylistManager playlistManager;

    public MyTunesController() {

        try {

            this.playlistModel = new PlaylistModel();
            playlistManager = new PlaylistManager();


            songModel = new SongModel();

        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupTableViews();
        loadInPlaylists();
        setupEventListeners();


    }

    // Formats duration from seconds to a mm:ss string format.
    public String getDurationFormatted(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void setupTableViews() {
        //setup columns in table view
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        colDuration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getDurationFormatted(cellData.getValue().getDuration())));


        tblPlaylist.setItems(playlistModel.getPlaylists());
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colSongs.setCellValueFactory(new PropertyValueFactory<>("SongsAmount"));
        colSongsDuration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getDurationFormatted(cellData.getValue().getPlaylistTotalDuration())));

        //binding songs to song table
        tblSongs.setItems(songModel.getObservableSongs());


        txtSongSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                songModel.searchSong(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    // Loads playlists into the table view.
    private void loadInPlaylists() {
        playlistModel.loadInPlaylists();
        tblPlaylist.refresh();
    }

    //creating new playlist
    public void createNewPlaylist(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Playlist");
        dialog.setHeaderText("Enter the name of the new playlist:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(playlistName -> {
            try {
                playlistModel.createPlaylist(playlistName);
                loadInPlaylists(); // Reload or refresh the list
            } catch (Exception e) {
                e.printStackTrace(); // Or handle this more gracefully
            }
        });
    }

    // Opens a dialog to update selected song.
    public void updateSong(ActionEvent actionEvent) throws Exception {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            try {
                // Opens a dialog with the selected song for editing.
                Optional<Song> result = dialogboxes.showSongDialog(true, selectedSong);

                result.ifPresent(updatedSong -> {
                    try {
                        songModel.updateSong(updatedSong); // Update in the model
                        tblSongs.refresh(); // Refresh the table displaying songs
                        if (tblSongsOnPlaylist.getItems().contains(updatedSong)) {
                            tblSongsOnPlaylist.refresh(); // Refresh playlist table if needed
                        }
                    } catch (Exception e) {
                        showErrorAlert("Update Error", "Failed to update the song: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                showErrorAlert("Dialog Error", "Failed to open the song dialog: " + e.getMessage());
            }
        } else {
            showInfoAlert("No Song Selected", "Please select a song to update.");
        }
        /*
        tblSongsOnPlaylist.setItems(songModel.getObservableSongs());
        if (selectedSong != null) {
            // If update is pressed the boolean "isUpdating" returns true in order to differentiate between update and create.
            Optional<Song> result = dialogboxes.showSongDialog(true, selectedSong);
            result.ifPresent(song -> {
                try {
                    songModel.updateSong(song);
                    tblSongs.refresh();
                    tblSongsOnPlaylist.refresh();

                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
        } else { //Displays message when no song is selected
            if (selectedSong == null) {
                //showAlert("No song selected", "Please select a song to update.");
            }
        }
          */

    }

    // Opens a dialog to create new songs.
    public void addSong(ActionEvent actionEvent) throws Exception {
        try {
            // Attempt to show the dialog and get the result
            Optional<Song> result = dialogboxes.showSongDialog(false, null);

            // Handle the result, if present
            result.ifPresent(newSong -> {
                try {
                    songModel.addSong(newSong); // Add the new song to the model
                    tblSongs.refresh(); // Refresh the table displaying songs
                } catch (Exception e) {
                    showErrorAlert("Creation Error", "Failed to add the new song: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showErrorAlert("Dialog Error", "Failed to open the song dialog: " + e.getMessage());
        }
    }



    // Helper methods for displaying alerts
    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Onplay and onStop actionevents and read songs in song tableveiw
    public void onPlay(ActionEvent actionEvent) {
        // Retrieve all song paths from the TableView.
        ObservableList<Song> items = tblSongs.getItems();
        if (items.isEmpty()) {
            System.out.println("No songs available to play!");
            return;
        }

        if (mediaPlayer != null) {
            // If mediaPlayer is paused, resume playback
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY) {
                mediaPlayer.play();
                System.out.println("Resumed playback.");
                return;
                }
            else if (status == MediaPlayer.Status.PLAYING) {
                System.out.println("Already playing, moron.");
                return;
            }


        }
        
        songPaths = items.stream().toList(); // Convert to a List<String>.
        currentSongIndex = 0; // Reset to the start of the playlist.

        playSong(currentSongIndex);
    }

    private void playSong(int index)  {
        if (index >= songPaths.size()) {
            System.out.println("End of playlist.");
            return; // Stop if we reach the end of the playlist.
        }
try{
        Song Song = songPaths.get(index);

        String path = folder + Song.getFilePath();
        System.out.println("Now playing: " + path);
        crntTrackTxt.setText("Current Track: " + Song.getTitle() + " - " + Song.getArtist());

        // Create Media and MediaPlayer for the current song.
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        // Start playback.
        mediaPlayer.play();

        // Set callback to play the next song after the current one ends.
        mediaPlayer.setOnEndOfMedia(() -> {
            currentSongIndex++;
            playSong(currentSongIndex);

        });
} catch (Exception e) {
    System.out.println( e.getMessage());
}
    }

    public void onStop(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void onNext(ActionEvent actionEvent) {
        mediaPlayer.stop();
        currentSongIndex++;
        playSong(currentSongIndex);
    }

    public void onPrevious(ActionEvent actionEvent) {
        mediaPlayer.stop();
        currentSongIndex--;
        playSong(currentSongIndex);
    }



    private void setupEventListeners() {
        tblPlaylist.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                playlistModel.loadSongsForPlaylist(newSelection.getId());
            }
        });

    }

    public void addSongToPlaylist(ActionEvent actionEvent) {
        // Retrieve the selected song and playlist
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tblPlaylist.getSelectionModel().getSelectedItem();

        if (selectedSong == null || selectedPlaylist == null) {
            showInfoAlert("Selection Required", "Please select both a song and a playlist.");
            return;
        }

        try {
            // Call the PlaylistModel to handle the addition
            playlistModel.addSongToPlaylist(selectedPlaylist.getId());
            playlistModel.loadSongsForPlaylist(selectedPlaylist.getId()); // Refresh songs for the playlist
            tblSongsOnPlaylist.refresh();
            showInfoAlert("Song Added", "The song has been successfully added to the playlist.");
        } catch (Exception e) {
            showErrorAlert("Error Adding Song", "An error occurred while adding the song to the playlist: " + e.getMessage());
        }
    }

}

