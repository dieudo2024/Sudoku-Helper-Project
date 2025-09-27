module com.example.sudokuhelper1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokuhelper1 to javafx.fxml;
    exports com.example.sudokuhelper1;
}