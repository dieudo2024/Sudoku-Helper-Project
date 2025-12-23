package com.example.sudokuhelper.Model;

import javafx.scene.control.TextField;

/**
 * Utility class that centralizes TextField styling for Sudoku cells.
 * Methods set visual styles and enable/disable state for different cell roles.
 */
public class StyleManager {

    /** Marks a TextField as a given (original puzzle cell). */
    public static void markGiven(TextField tf) {
        tf.getStyleClass().removeAll("cell", "cell-solved", "cell-selected");
        if (!tf.getStyleClass().contains("cell-given")) tf.getStyleClass().add("cell-given");
        tf.setDisable(true);
    }

    /** Marks a TextField as editable (player can change it). */
    public static void markEditable(TextField tf) {
        tf.getStyleClass().removeAll("cell-given", "cell-solved", "cell-selected");
        if (!tf.getStyleClass().contains("cell")) tf.getStyleClass().add("cell");
        tf.setDisable(false);
    }

    /** Marks a TextField as solved (final value displayed). */
    public static void markSolved(TextField tf) {
        tf.getStyleClass().removeAll("cell", "cell-given", "cell-selected");
        if (!tf.getStyleClass().contains("cell-solved")) tf.getStyleClass().add("cell-solved");
        tf.setDisable(true);
    }

    /** Highlights a TextField as currently selected. */
    public static void markSelected(TextField tf) {
        if (!tf.getStyleClass().contains("cell-selected")) tf.getStyleClass().add("cell-selected");
    }

    /** Removes the selected highlight from a TextField. */
    public static void clearSelection(TextField tf) {
        tf.getStyleClass().remove("cell-selected");
    }
}
