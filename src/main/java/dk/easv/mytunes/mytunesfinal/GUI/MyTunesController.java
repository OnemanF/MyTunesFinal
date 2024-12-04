package dk.easv.mytunes.mytunesfinal.GUI;

import dk.easv.mytunes.mytunesfinal.BE.Playlist;
import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.BLL.PlaylistManager;
import dk.easv.mytunes.mytunesfinal.GUI.Model.PlaylistModel;
import dk.easv.mytunes.mytunesfinal.GUI.Model.SongModel;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.MediaPlayer;

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java
import java.awt.event.ActionListener;
=======
import javax.swing.plaf.basic.BasicOptionPaneUI;
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java
import java.net.URL;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable{
    //Table view
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    private TableView<Playlist> tblPlaylist;
    //Table columns
    @FXML
    private TableColumn<Song, String> colTitle;
    @FXML
    private TableColumn<Song, String> colArtist;
    @FXML
    private TableColumn<Song, String> colGenre;
    @FXML
    private TableColumn<Song, String> colDuration;

    //playlist table
    @FXML
    private TableColumn<Playlist, String> colName, colSongs, colSongsDuration;

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java
    @FXML
    private MediaPlayer mediaPlayer;
=======
    //media player
    @FXML
    private
    MediaPlayer mediaPlayer;
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java

    //search field
    @FXML
    private TextField txtSongSearch;

    @FXML
    private Slider songProgressSlider, volumeSlider;

    //buttons
    @FXML
    private Button searchButton;

    @FXML
    private Button playButton = new Button();


    public void playButton(ActionEvent event) {
        mediaPlayer.playMedia("Haddaway - What Is Love [Official 4K].mp3");
    }


<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java
=======
    @FXML
    private Button playButton = new Button();

    @FXML
    private Button playButton;

    @FXML
    private AddUpdateSong dialogboxes = new AddUpdateSong();
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java
    private SongModel songModel;
    private PlaylistModel playlistModel;

    private PlaylistManager playlistManager;

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java

    public void playButton(ActionEvent event) {
        mediaPlayer.playMedia("Haddaway - What Is Love [Official 4K].mp3");
    }
=======
    mediaPlayer mediaPlayer = new mediaPlayer();
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java

    public MyTunesController() {

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java

=======
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java
        mediaPlayer = new MediaPlayer();

        try {

            this.playlistModel = new PlaylistModel();
            playlistManager = new PlaylistManager();


            songModel = new SongModel();

        }catch (Exception e) {
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
        setupMediaControls();


    }

    // Formats duration from seconds to a mm:ss string format.
    public String getDurationFormatted(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void setupTableViews(){
        //setup columns in table view
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        //colDuration.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        colDuration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getDurationFormatted(cellData.getValue().getDuration())));


        tblPlaylist.setItems(playlistModel.getPlaylists());
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colSongs.setCellValueFactory(new PropertyValueFactory<>("totalSongs"));
        colSongsDuration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getDurationFormatted(cellData.getValue().getPlaylistTotalDuration())));


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

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java
=======
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
    public void UpdateTheSongs(ActionEvent actionEvent) throws Exception {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        tblSongsInPlaylist.setItems(songModel.getObservableSongs());
        if (selectedSong != null) {
            // If update is pressed the boolean "isUpdating" returns true in order to differentiate between update and create.
            Optional<Song> result = dialogboxes.showSongDialog(true, selectedSong);
            result.ifPresent(song -> {
                try {
                    songModel.updateSong(song);
                    tblSongs.refresh();
                    tblSongsInPlaylist.refresh();

                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
        }else{ //Displays message when no song is selected
            if (selectedSong == null) {
                //showAlert("No song selected", "Please select a song to update.");
            }
        }
    }

    // Opens a dialog to create new songs.
    public void CreateSong(ActionEvent actionEvent) throws Exception {
        Optional<Song> result = dialogboxes.showSongDialog(false, null);

        result.ifPresent(song -> {
            try {
                songModel.CreateSong(song);
            } catch (Exception e) {
                e.printStackTrace(); // Or handle the exception in another way
            }
        });
    }
    private void setupMediaControls() {

    }

        // play the selected song.
        public void playButton (ActionEvent actionEvent) {
                // Find which song should be played
                mediaPlayer.playMedia("Haddaway - What Is Love [Official 4K].mp3");

        }

>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java

<<<<<<< Updated upstream:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/MyTunesController.java

}
=======
}
>>>>>>> Stashed changes:src/main/java/dk/easv/mytunes/mytunesfinal/GUI/Controller/MyTunesController.java
