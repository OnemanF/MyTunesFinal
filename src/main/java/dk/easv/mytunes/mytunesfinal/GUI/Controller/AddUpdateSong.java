package dk.easv.mytunes.mytunesfinal.GUI.Controller;

import dk.easv.mytunes.mytunesfinal.BE.Song;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

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
        ComboBox<String> genreComboBox = new ComboBox<>();
        TextField timeField = new TextField();
        TextField filePathField = new TextField();
        Button chooseFileButton = new Button("Choose File");

        // Initialize the genre combo box
        //GenreDAO_DB genreDAO = new GenreDAO_DB();
        //genreComboBox.getItems().addAll(genreDAO.getAllGenre().stream().map(Genre::getGenreType).collect(Collectors.toList()));

        // Configure layout
        GridPane grid = createFormGrid(titleField, artistField, genreComboBox, timeField, filePathField, chooseFileButton);

        // Populate fields if updating
        if (isUpdating && selectedSong != null) {
            populateFields(selectedSong, titleField, artistField, genreComboBox, timeField, filePathField);
        }

        // Handle file selection
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        chooseFileButton.setOnAction(e -> handleFileSelection(fileChooser, filePathField, titleField, artistField, genreComboBox, timeField));

        dialog.getDialogPane().setContent(grid);

        // Disable button if any field is empty
        Node actionButtonNode = dialog.getDialogPane().lookupButton(actionButton);
        actionButtonNode.disableProperty().bind(isAnyFieldEmpty(titleField, artistField, genreComboBox, timeField, filePathField));

        // Handle result conversion
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == actionButton) {
                return createSong(isUpdating, selectedSong, titleField, artistField, genreComboBox, timeField, filePathField);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    // Helper methods to keep the code clean
    private GridPane createFormGrid(TextField titleField, TextField artistField, ComboBox<String> genreComboBox,
                                    TextField timeField, TextField filePathField, Button chooseFileButton) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Artist:"), 0, 1);
        grid.add(artistField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        grid.add(genreComboBox, 1, 2);
        grid.add(new Label("Time:"), 0, 3);
        grid.add(timeField, 1, 3);
        grid.add(new Label("File:"), 0, 4);
        grid.add(filePathField, 1, 4);
        grid.add(chooseFileButton, 2, 4); // File chooser button
        return grid;
    }

    private void populateFields(Song selectedSong, TextField titleField, TextField artistField, ComboBox<String> genreComboBox,
                                TextField timeField, TextField filePathField) {
        titleField.setText(selectedSong.getTitle());
        artistField.setText(selectedSong.getArtist());
        genreComboBox.setValue(selectedSong.getGenre());
        timeField.setText(String.valueOf(selectedSong.getLength()));
        filePathField.setText(selectedSong.getFilePath());
    }

    private void handleFileSelection(FileChooser fileChooser, TextField filePathField, TextField titleField,
                                     TextField artistField, ComboBox<String> genreComboBox, TextField timeField) {
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getName());
            extractMetadataFromFile(selectedFile, titleField, artistField, genreComboBox, timeField);
        }
    }

    private void extractMetadataFromFile(File file, TextField titleField, TextField artistField,
                                         ComboBox<String> genreComboBox, TextField timeField) {
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            titleField.setText(media.getMetadata().get("title") != null ? (String) media.getMetadata().get("title") : "");
            artistField.setText(media.getMetadata().get("artist") != null ? (String) media.getMetadata().get("artist") : "");
            genreComboBox.setValue(""); // Genre is rarely in the metadata
            timeField.setText(String.valueOf((int) media.getDuration().toSeconds()));
        });
    }

    private BooleanBinding isAnyFieldEmpty(TextField titleField, TextField artistField, ComboBox<String> genreComboBox,
                                           TextField timeField, TextField filePathField) {
        return titleField.textProperty().isEmpty()
                .or(artistField.textProperty().isEmpty())
                .or(genreComboBox.valueProperty().isNull())
                .or(timeField.textProperty().isEmpty())
                .or(filePathField.textProperty().isEmpty());
    }

    private Song createSong(boolean isUpdating, Song selectedSong, TextField titleField, TextField artistField,
                            ComboBox<String> genreComboBox, TextField timeField, TextField filePathField) {
        if (isUpdating) {
            return new Song(selectedSong.getId(),
                    artistField.getText(),
                    titleField.getText(),
                    genreComboBox.getValue(),
                    Integer.parseInt(timeField.getText()),
                    filePathField.getText());
        } else {
            return new Song(
                    artistField.getText(),
                    titleField.getText(),
                    genreComboBox.getValue(),
                    Integer.parseInt(timeField.getText()),
                    filePathField.getText());
        }
    }
}
