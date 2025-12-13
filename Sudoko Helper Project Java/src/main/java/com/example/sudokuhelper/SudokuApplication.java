package com.example.sudokuhelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX application entry point for the Sudoku helper app.
 */
public class SudokuApplication extends Application {
    /**
     * Starts the JavaFX application by loading the FXML view and showing the stage.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("sudoku-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 555);
        // load application stylesheet
        var css = SudokuApplication.class.getResource("sudoku.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();
    }

    /** Standard launcher used by the build/run tooling. */
    public static void main(String[] args) {
        launch(args);
    }
}