package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

//import javax.print.attribute.standard.Media;
import java.io.File;
import java.util.Optional;

public class AddUpdateSong {

    public Optional<Song> showSongDialog(boolean Updating, Song selectedSong) {

        // Create and configure the pop-up
        Dialog<Song> dialog = new Dialog<>();
        dialog.setTitle(Updating ? "Update Song" : "Add New Song");
        ButtonType actionButton = new ButtonType(Updating ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(actionButton, ButtonType.CANCEL);

        // Create input elements
        TextField titleField = new TextField();
        TextField artistField = new TextField();
        TextField genreField = new TextField();
        //ComboBox<String> genreComboBox = new ComboBox<>();
        TextField durationField = new TextField();
        TextField filePathField = new TextField();
        Button chooseFileButton = new Button("Choose File");

        // Initialize the genre combo box
        //GenreDAO_DB genreDAO = new GenreDAO_DB();
        //genreComboBox.getItems().addAll(genreDAO.getAllGenre().stream().map(Genre::getGenreType).collect(Collectors.toList()));

        // Configure layout
        GridPane grid = createFormGrid(titleField, artistField, genreField, durationField, filePathField, chooseFileButton);

        // Populate fields if updating
        if (Updating && selectedSong != null) {
            populateFields(selectedSong, titleField, artistField, genreField, durationField, filePathField);
        }

        // Handles file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        chooseFileButton.setOnAction(e -> handleFileSelection(fileChooser, titleField, artistField, genreField, durationField, filePathField));

        dialog.getDialogPane().setContent(grid);

        // Disable button if any field is empty
        Node actionButtonNode = dialog.getDialogPane().lookupButton(actionButton);
        actionButtonNode.disableProperty().bind(isAnyFieldEmpty(titleField, artistField, genreField, durationField, filePathField));

        // Handle conversion
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == actionButton) {
                return createSong(Updating, selectedSong, titleField, artistField, genreField, durationField, filePathField);
            }
            return null;
        });

        return dialog.showAndWait();
    }


    // Helper methods to keep the code clean
    private GridPane createFormGrid(TextField titleField, TextField artistField,
                                    TextField durationField, TextField filePathField, TextField pathField, Button chooseFileButton) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Artist:"), 0, 1);
        grid.add(artistField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        //grid.add(genreComboBox, 1, 2);
        grid.add(new Label("Time:"), 0, 3);
        grid.add(durationField, 1, 3);
        grid.add(new Label("File:"), 0, 4);
        grid.add(filePathField, 1, 4);
        grid.add(chooseFileButton, 2, 4); // File chooser button
        return grid;
    }



    private void populateFields(Song selectedSong, TextField titleField, TextField artistField,
                                TextField durationField, TextField filePathField, TextField pathField) {
        titleField.setText(selectedSong.getTitle());
        artistField.setText(selectedSong.getArtist());
        //genreComboBox.setValue(selectedSong.getGenre());
        durationField.setText(String.valueOf(selectedSong.getDuration()));
        filePathField.setText(selectedSong.getFilePath());
    }

    private void handleFileSelection(FileChooser fileChooser, TextField filePathField, TextField titleField,
                                     TextField artistField, TextField durationField, TextField field) {
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getName());
            extractMetadataFromFile(selectedFile, titleField, artistField, durationField);
        }
    }


    private void extractMetadataFromFile(File file, TextField titleField, TextField artistField,
                                         TextField durationField) {
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            titleField.setText(media.getMetadata().get("title") != null ? (String) media.getMetadata().get("title") : "");
            artistField.setText(media.getMetadata().get("artist") != null ? (String) media.getMetadata().get("artist") : "");
            //genreComboBox.setValue(""); // Genre is rarely in the metadata
            durationField.setText(String.valueOf((int) media.getDuration().toSeconds()));
        });
    }



    private BooleanBinding isAnyFieldEmpty(TextField titleField, TextField artistField,
                                           TextField durationField, TextField filePathField, TextField pathField) {
        return titleField.textProperty().isEmpty()
                .or(artistField.textProperty().isEmpty())
               // .or(genreComboBox.valueProperty().isNull())
                .or(durationField.textProperty().isEmpty())
                .or(filePathField.textProperty().isEmpty());
    }

    private Song createSong(boolean Updating, Song selectedSong, TextField artistField, TextField titleField,
                            TextField durationField, TextField genreField, TextField filePathField) {
        int duration = Integer.parseInt(durationField.getText());
        if (Updating) {
            return new Song(selectedSong.getId(),
                    artistField.getText(),
                    titleField.getText(),
                    genreField.getText(),
                    //genreComboBox.getValue(),
                    duration,
                    filePathField.getText());

        } else {
            return new Song(
                    0,
                    artistField.getText(),
                    titleField.getText(),
                    genreField.getText(),
                    //genreComboBox.getValue(),
                    Integer.parseInt(durationField.getText()),
                    filePathField.getText());
        }
    }


}
