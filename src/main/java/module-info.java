module dk.easv.mytunes.mytunesfinal {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.mytunes.mytunesfinal to javafx.fxml;
    exports dk.easv.mytunes.mytunesfinal;
}