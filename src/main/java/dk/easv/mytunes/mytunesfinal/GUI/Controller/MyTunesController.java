package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.PlaylistManager;
import dk.easv.mytunes.mytunesfinal.DAO.db.PlaylistDAO_DB;
import dk.easv.mytunes.mytunesfinal.GUI.Model.PlaylistModel;
import dk.easv.mytunes.mytunesfinal.GUI.Model.SongModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
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
    private TableColumn<Song, String> colTitleOnplaylist, colSongsArtist;

    @FXML
    private Button deleteSong;

    //playlist table
    @FXML
    private TableColumn<Playlist, String> colName, colSongs, colSongsDuration;

    //search field
    @FXML
    private TextField txtSongSearch;

    @FXML
    private Label crntTrackTxt;

    @FXML
    private Label progressLbl;

    //slider
    @FXML
    private Slider volumeSlider;

    //progressbar
    @FXML
    private ProgressBar progressBar;

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

    private final ObservableList<Song> songsOnPlaylist = FXCollections.observableArrayList();
    private PlaylistDAO_DB playlistDAO;
    private PlaylistManager playlistManager;
    private MediaPlayerController mediaPlayerController;


    public MyTunesController() {
        try {
            this.playlistModel = new PlaylistModel();
            playlistManager = new PlaylistManager();
            playlistDAO = new PlaylistDAO_DB();
            this.mediaPlayerController = new MediaPlayerController(playlistDAO);

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
        loadPlaylists();
        setupEventListeners();
        setupPlaylistSelectionListener();
        volumeSlider.setValue(0.5);
        setupDoubleClickToPlay();
        setupDoubleClickToPlaySongs();
    }





    // Formats duration from seconds to a mm:ss string format.
    public String getDurationFormatted(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void setupTableViews() {

        colTitleOnplaylist.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colSongsArtist.setCellValueFactory(new PropertyValueFactory<>("Artist"));

        // Binding songs to tblSongsOnPlaylist
        tblSongsOnPlaylist.setItems(songsOnPlaylist);

        // song TableView columns setup
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        colDuration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getDurationFormatted(cellData.getValue().getDuration())));

        // Playlist TableView columns setup
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

    private void setupPlaylistSelectionListener() {
        // Add selection listener for playlist TableView
        tblPlaylist.setOnMouseClicked(event -> {
            Playlist selectedPlaylist = tblPlaylist.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                int playlistID = selectedPlaylist.getId();
                List<Song> fetchedSongs = playlistDAO.getSongsForPlaylist(playlistID);
                songsOnPlaylist.setAll(fetchedSongs);
            }
        });
    }

    // Loads playlists into the table view.
    private void loadPlaylists() {
        playlistModel.loadPlaylists();
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
                loadPlaylists(); // Reload or refresh the list
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

    public void playTheSong(int index) {
        Song song = playlistDAO.getSongById(index); // Fetch the song by ID
        if (song != null) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop(); // Stop the current song
                }

                String fullPath = folder + song.getFilePath();
                File file = new File(fullPath);

                if (!file.exists()) {
                    System.out.println("File not found: " + fullPath);
                    return; // Exit if the file does not exist
                }

                // Create a new Media object with the song's file path
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

                System.out.println("Now playing: " + song.getTitle());
            } catch (Exception e) {
                System.out.println("Error playing song: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Song with ID " + index + " not found.");
        }
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
        setupVolume();
        setupProgressBar();

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
        try {
            // Retrieve and validate the selected song and playlist
            Song selectedSong = getSelectedSong();
            Playlist selectedPlaylist = getSelectedPlaylist();

            // Add the song to the playlist
            playlistModel.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());

            // Refresh the playlist view
            refreshPlaylistView(selectedPlaylist);

            // Notify the user
            showInfoAlert("Song Added", "The song has been successfully added to the playlist.");
        } catch (IllegalArgumentException e) {
            showInfoAlert("Selection Required", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Error Adding Song", "An error occurred while adding the song to the playlist: " + e.getMessage());
        }
    }

    // Helper method to retrieve the selected song
    private Song getSelectedSong() {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        if (selectedSong == null) {
            throw new IllegalArgumentException("Please select a song.");
        }
        return selectedSong;
    }

    // Helper method to retrieve the selected playlist
    private Playlist getSelectedPlaylist() {
        Playlist selectedPlaylist = tblPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            throw new IllegalArgumentException("Please select a playlist.");
        }
        return selectedPlaylist;
    }

    // Helper method to refresh the playlist view
    private void refreshPlaylistView(Playlist selectedPlaylist) {
        playlistModel.loadSongsForPlaylist(selectedPlaylist.getId()); // Refresh songs for the playlist
        tblSongsOnPlaylist.refresh();
    }


    public void setupVolume() {

            volumeSlider.setMin(0.0); // Set minimum value to nothing
            volumeSlider.setMax(1.0); // Set max value to full
            volumeSlider.setBlockIncrement(0.05); // Set the value for increments
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volumeSlider.getValue());  // Set initial volume
                volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    mediaPlayer.setVolume(newValue.doubleValue()); // Listen for change in slider
                });
            }
    }

    public void setupProgressBar(){
        progressBar.setProgress(mediaPlayer.getCurrentTime().toSeconds() / 100);

        // Create a Timeline to update the ProgressBar every 100ms
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    // Calculate the progress based on current time and total duration
                    double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                    double totalDuration = mediaPlayer.getTotalDuration().toSeconds();

                    // Set the progress to the ratio of current time over total duration
                    if (totalDuration > 0) {
                        progressBar.setProgress(currentTime / totalDuration);

                        // Converts double from toSeconds into our chosen format of "00:00"
                        int dingusTime = (int) mediaPlayer.getCurrentTime().toSeconds();
                        int dingusTotal = (int) mediaPlayer.getTotalDuration().toSeconds();

                        // Converts current and total time to strings for use in label under progressbar
                        String displayTime = String.format (getDurationFormatted(dingusTime));
                        String totalTime = String.format (getDurationFormatted(dingusTotal));
                        // Sets label under bar to given values
                        progressLbl.setText(displayTime + " / " + totalTime);

                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Keep updating indefinitely
        timeline.play(); // Start the timeline
    }


    @FXML
    private void deletePlaylist(ActionEvent actionEvent) {
        Playlist selectedPlaylist = tblPlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Playlist");
            confirmation.setHeaderText("Are you sure you want to delete this playlist?");
            confirmation.setContentText("Playlist" + selectedPlaylist.getName());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    playlistManager.deletePlaylist(selectedPlaylist);
                    playlistModel.getPlaylists().remove(selectedPlaylist);
                    tblPlaylist.refresh();

                    showInfoAlert("Playlist Deleted", "The playlist has been successfully deleted.");
                } catch (Exception e) {
                    showErrorAlert("Error", "could not delete playlist:" + e.getMessage());
                }
            } else {
                showInfoAlert("No Playlist Selected", "Please select a playlist to delete.");
            }
        }
    }
    @FXML
    private void deleteSong(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Song");
            confirmation.setHeaderText("Are you sure you want to delete this song?");
            confirmation.setContentText("Song:" + selectedSong.getTitle());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    songModel.deleteSong(selectedSong);
                    tblSongs.refresh();
                    showInfoAlert("Song Deleted", "The song has been successfully deleted.");
                } catch (Exception e) {
                    showErrorAlert("Error", "could not delete song:" + e.getMessage());
                }
            }
        } else {
            showInfoAlert("No Song Selected", "Please select a playlist to delete.");
        }
    }

    public void editPlaylist(ActionEvent actionEvent) {
        Playlist selectedPlaylist = tblPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            TextInputDialog dialog = new TextInputDialog(selectedPlaylist.getName());
            dialog.setTitle("Edit Playlist");
            dialog.setHeaderText("Enter the new name for the playlist:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newPlaylistName -> {
                try {
                    playlistModel.editPlaylist(selectedPlaylist.getId(), newPlaylistName);
                    loadPlaylists(); // Reload or refresh the list
                } catch (Exception e) {
                    e.printStackTrace(); // Or handle this more gracefully
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Playlist Selected");
            alert.setHeaderText("Please select a playlist to edit.");
            alert.setContentText("You must select a playlist from the table before attempting to edit.");
            alert.showAndWait();
            }
        }

        public void onBtnClose(ActionEvent actionEvent) {
            Platform.exit();
        }
    @FXML
    private void removeSongFromPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tblPlaylist.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Remove Song");
            confirmation.setHeaderText("Are you sure you want to remove this song from the playlist?");
            confirmation.setContentText("Song: " + selectedSong.getTitle());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    playlistModel.removeSongFromPlaylist(selectedPlaylist.getId(), selectedSong.getId());
                    playlistModel.loadSongsForPlaylist(selectedPlaylist.getId());
                    tblSongsOnPlaylist.refresh();
                    showInfoAlert("Song Removed", "The song has been successfully removed from the playlist.");
                } catch (Exception e) {
                    showErrorAlert("Error", "Could not remove song from playlist: " + e.getMessage());
                }
            }
        } else {
            showInfoAlert("No Song Selected", "Please select a song from the playlist to remove.");
        }
    }

    private void setupDoubleClickToPlay(){
        //double-click handler for tblSongsOnPlaylist
        tblSongsOnPlaylist.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount()==2){
                Song selectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    playTheSong(selectedSong.getId());
                } else {
                    System.out.println("No song selected.");
                }
            }
        });
    }

    private void setupDoubleClickToPlaySongs(){
        //double-click handler for tblSongsOnPlaylist
        tblSongs.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount()==2){
                Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    playTheSong(selectedSong.getId());
                } else {
                    System.out.println("No song selected.");
                }
            }
        });
    }

}

