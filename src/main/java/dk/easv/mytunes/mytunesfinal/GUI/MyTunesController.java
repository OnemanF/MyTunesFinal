package dk.easv.mytunes.mytunesfinal.GUI;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import dk.easv.mytunes.mytunesfinal.GUI.Model.SongModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable{
    //Table view
    @FXML
    private TableView<Song> tblSongs;
    //Table columns
    @FXML
    private TableColumn<Song, String> colTitle;
    @FXML
    private TableColumn<Song, String> colArtist;
    @FXML
    private TableColumn<Song, String> colGenre;
    @FXML
    private TableColumn<Song, String> colDuration;

    //search field
    @FXML
    private TextField txtSongSearch;

    //buttons
    @FXML
    private Button searchButton;

    private SongModel songModel;

    public MyTunesController() {

        try {

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



    }

    private void setupTableViews(){
        //setup columns in table view
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
       // colDuration.setCellValueFactory(new PropertyValueFactory<>("Duration"));

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



}