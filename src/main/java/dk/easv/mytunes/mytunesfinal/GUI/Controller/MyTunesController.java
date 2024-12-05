package dk.easv.mytunes.mytunesfinal.GUI.Controller;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable{
    //Table view
    @FXML
    private TableView<Song> tblSongsOnPlaylist;
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
    @FXML
    private TableColumn<Song, String> colSong, colSongsArtist;

    //playlist table
    @FXML
    private TableColumn<Playlist, String> colName, colSongs, colSongsDuration;



    //search field
    @FXML
    private TextField txtSongSearch;

    //buttons
    @FXML
    private Button searchButton, btnNewPlaylist;

    @FXML
    private Button btnAddNewSong, btUpdateSong;

    @FXML
    private AddUpdateSong dialogboxes = new AddUpdateSong();

    private SongModel songModel;
    private PlaylistModel playlistModel;

    private PlaylistManager playlistManager;

    public MyTunesController() {

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
        colSongs.setCellValueFactory(new PropertyValueFactory<>("SongsAmount"));
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
        }else{ //Displays message when no song is selected
            if (selectedSong == null) {
                //showAlert("No song selected", "Please select a song to update.");
            }
        }
    }

    // Opens a dialog to create new songs.
    public void addSong(ActionEvent actionEvent) throws Exception {

        Optional<Song> result = dialogboxes.showSongDialog(false, null);

        result.ifPresent(newSong -> {
            try {
                songModel.addSong(newSong);
            } catch (Exception e) {
                e.printStackTrace(); // Or handle the exception in another way
            }
        });
    }

    //IMPLEMENT
    public void onPlay(ActionEvent actionEvent) {

        String path = "music/Ariana Grande - Santa Tell Me (Official Video).mp3";

        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        System.out.println("onPlay");
    }
}
