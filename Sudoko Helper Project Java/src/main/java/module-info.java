module com.example.sudokuhelper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokuhelper to javafx.fxml;
    opens com.example.sudokuhelper.Controller to javafx.fxml;
    exports com.example.sudokuhelper;
}