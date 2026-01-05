package com.example.sudokuhelper.Controller;

/**
 * JavaFX controller for the Sudoku UI.
 * <p>Handles user interactions (button clicks, import, analyze, solve) and
 * delegates game logic to {@link com.example.sudokuhelper.Model.SudokuModel}.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.sudokuhelper.Model.GridCell;
import com.example.sudokuhelper.Model.InputValidator;
import com.example.sudokuhelper.Model.SolutionChecker;
import com.example.sudokuhelper.Model.StyleManager;
import com.example.sudokuhelper.Model.SudokuModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SudokuController {

    private final SudokuModel model = new SudokuModel();

    @FXML
    private Button buttonOne;
    @FXML
    private Button buttonTwo;
    @FXML
    private Button buttonThree;
    @FXML
    private Button buttonFour;
    @FXML
    private Button buttonFive;
    @FXML
    private Button buttonSix;
    @FXML
    private Button buttonSeven;
    @FXML
    private Button buttonEight;
    @FXML
    private Button buttonNine;
    @FXML
    private Button importButton;

    @FXML
    private GridPane gridPane;

    private final TextField[][] gridTextField = new TextField[9][9];

    private final int[][] player = new int[9][9];

    /**
     * JavaFX initialization hook. Builds the 9x9 TextField grid, attaches listeners
     * and configures keyboard handling.
     */
    @FXML
    private void initialize() {
        buildGrid();
        configureGridPane();
        styleButtons();
        DisplayGrid();
        updatePlayerArray();
        possibleValues.clear();
        enableDisableNumberButtons();
    }

    private TextField selectedTextField = null;
    /**
     * Reads the current values from the TextField grid into the internal player array
     * and synchronizes the model's player grid.
     */
    private void updatePlayerArray() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = gridTextField[row][col].getText().trim();
                if (InputValidator.isValidDigit(text)) player[row][col] = Integer.parseInt(text);
                else player[row][col] = 0; // Empty or invalid cells become 0
            }
        }
        // keep model in sync
        model.setPlayerGrid(player);
    }

    /** Highlights the row, column and 3x3 box for the selected cell. */
    private void highlightRelatedCells(int selRow, int selCol) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                TextField tf = gridTextField[r][c];
                // skip the selected cell itself (it has its own class)
                if (r == selRow && c == selCol) continue;
                boolean sameRow = (r == selRow);
                boolean sameCol = (c == selCol);
                boolean sameBox = ((r / 3) == (selRow / 3) && (c / 3) == (selCol / 3));
                if (sameRow || sameCol || sameBox) {
                    if (!tf.getStyleClass().contains("cell-highlight")) tf.getStyleClass().add("cell-highlight");
                } else {
                    tf.getStyleClass().remove("cell-highlight");
                }
            }
        }
    }

    private void buildGrid() {
        gridPane.getChildren().clear();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField tf = createCell(row, col);
                gridTextField[row][col] = tf;
                gridPane.add(tf, col, row);
            }
        }
    }

    private TextField createCell(int row, int col) {
        TextField tf = new TextField();
        tf.setPrefWidth(40);
        tf.setPrefHeight(40);
        tf.setFocusTraversable(false);
        StyleManager.markEditable(tf);

        if (row % 3 == 0) tf.getStyleClass().add("box-border-top");
        if (row % 3 == 2) tf.getStyleClass().add("box-border-bottom");
        if (col % 3 == 0) tf.getStyleClass().add("box-border-left");
        if (col % 3 == 2) tf.getStyleClass().add("box-border-right");

        tf.textProperty().addListener((observable, oldValue, newValue) -> handleTextChange(tf, oldValue, newValue, row, col));
        tf.setOnMouseClicked(e -> selectCell(tf, row, col));

        return tf;
    }

    private void handleTextChange(TextField tf, String oldValue, String newValue, int row, int col) {
        if (newValue.length() > 1) {
            tf.setText(newValue.substring(0, 1));
            return;
        }
        if (!newValue.isEmpty() && !InputValidator.isValidDigit(newValue)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter numbers from 1 to 9 only.");
            alert.showAndWait();
            tf.setText(oldValue);
            return;
        }
        updatePlayerArray();
        possibleValues = model.getPossibleValues(row, col);
        enableDisableNumberButtons();
    }

    private void selectCell(TextField tf, int row, int col) {
        if (selectedTextField != null) StyleManager.clearSelection(selectedTextField);
        selectedTextField = tf;
        StyleManager.markSelected(selectedTextField);
        highlightRelatedCells(row, col);
        possibleValues = model.getPossibleValues(row, col);
        enableDisableNumberButtons();
        tf.requestFocus();
    }

    private void configureGridPane() {
        gridPane.setFocusTraversable(true);
        if (!gridPane.getStyleClass().contains("sudoku-grid")) gridPane.getStyleClass().add("sudoku-grid");

        gridPane.setOnKeyPressed(event -> {
            if (selectedTextField == null || selectedTextField.isDisabled()) return;
            String key = event.getText();
            if (InputValidator.isValidDigit(key)) {
                selectedTextField.setText(key);
            } else if (event.getCode().toString().equals("BACK_SPACE") || event.getCode().toString().equals("DELETE")) {
                selectedTextField.clear();
            }
        });
    }

    private void styleButtons() {
        Button[] numButtons = {buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine};
        for (Button button : numButtons) {
            if (button == null) continue;
            if (!button.getStyleClass().contains("num-button")) button.getStyleClass().add("num-button");
        }
    }

    /** Helper method to fill the currently selected cell with {@code value}. */
    private void fillSelectedCell(int value) {
        if (selectedTextField != null && !selectedTextField.isDisabled()) {
            selectedTextField.setText(String.valueOf(value));
        }
        updatePlayerArray();
        enableDisableNumberButtons();
    }

    /** Inserts digit 1 into the selected cell. */
    @FXML
    public void onButtonOneClick(ActionEvent event) {
        fillSelectedCell(1);
    }
    /** Inserts digit 2 into the selected cell. */
    @FXML
    public void handleButtonTwo(ActionEvent event) {
        fillSelectedCell(2);
    }
    /** Inserts digit 3 into the selected cell. */
    @FXML
    public void handleButtonThree(ActionEvent event) {
        fillSelectedCell(3);
    }
    /** Inserts digit 4 into the selected cell. */
    @FXML
    public void handleButtonFour(ActionEvent event) {
        fillSelectedCell(4);
    }
    /** Inserts digit 5 into the selected cell. */
    @FXML
    public void handleButtonFive(ActionEvent event) {
        fillSelectedCell(5);
    }
    /** Inserts digit 6 into the selected cell. */
    @FXML
    public void handleButtonSix(ActionEvent event) {
        fillSelectedCell(6);
    }
    /** Inserts digit 7 into the selected cell. */
    @FXML
    public void handleButtonSeven(ActionEvent event) {
        fillSelectedCell(7);
    }
    /** Inserts digit 8 into the selected cell. */
    @FXML
    public void handleButtonEight(ActionEvent event) {
        fillSelectedCell(8);
    }
    /** Inserts digit 9 into the selected cell. */
    @FXML
    public void handleButtonNine(ActionEvent event) {
        fillSelectedCell(9);
    }
    /** Erases the currently selected cell. */
    @FXML
    public void onHandleEraseButtonClick(ActionEvent event) {
        handleErase();
        possibleValues.clear();
        enableDisableNumberButtons();
    }
    /** Trigger the solver for the current puzzle. */
    @FXML
    public void onHandleSolveButton(ActionEvent event) {
        handleSolve();
        updatePlayerArray();
        possibleValues.clear();
        enableDisableNumberButtons();
    }
    /** Show candidate values as tooltips for empty cells. */
    @FXML
    public void onHandleAnalyzeButton(ActionEvent event) {
        handleAnalyze();
    }

    /** Opens a file chooser and imports a Sudoku puzzle from a selected file. */
    @FXML
    public void onHandleImportButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Sudoku Grid");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt, *.csv)", "*.txt", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) importButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                boolean ok = model.importFromFile(selectedFile);
                if (ok) DisplayGrid();
                else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Import Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to import puzzle from file.");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Import Error");
                alert.setHeaderText(null);
                alert.setContentText("Error reading the file: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Import Canceled");
            alert.setHeaderText(null);
            alert.setContentText("File import was canceled.");
            alert.showAndWait();
        }
    }

    /** Validates the current player entries against the expected solution. */
    @FXML
    public void onHandleCheckSolutionButton(ActionEvent event) {
        handleCheckSolution();
    }

    /** Clears the selected cell and updates model state. */
    private void handleErase() {
        if (selectedTextField != null && !selectedTextField.isDisabled()) {
            selectedTextField.clear();
        }
        updatePlayerArray();
        enableDisableNumberButtons();
    }

    /** Attempts to solve the current puzzle using the model's solver. */
    private void handleSolve() {
        // Parse current UI into player array
        updatePlayerArray();

        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(player[r], 0, board[r], 0, 9);
        }

        // keep model in sync and use it to solve
        model.setPlayerGrid(board);
        boolean solved = model.solve();
        if (!solved || !SolutionChecker.isValidSolution(model.getPlayerGrid())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No Solution");
            alert.setHeaderText(null);
            alert.setContentText("No solution could be found for the current puzzle.");
            alert.showAndWait();
            return;
        }

        // Display solved board
        int[][] solvedBoard = model.getPlayerGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField tf = gridTextField[row][col];
                tf.setText(String.valueOf(solvedBoard[row][col]));
                StyleManager.markSolved(tf);
            }
        }
        selectedTextField = null;
    }

    /** Evaluates the player's solution and shows an alert with the result. */
    private void handleCheckSolution() {
        boolean solved = CheckSolution();
        if (solved && !SolutionChecker.isValidSolution(player)) solved = false;
        Alert alert = new Alert(solved ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle("Sudoku Result");
        alert.setHeaderText(null);
        alert.setContentText(solved ? "Congratulations! You solved the sudoku correctly." : "The solution is incorrect or incomplete. Keep trying!");
        alert.showAndWait();
    }

    List<Integer> possibleValues = new ArrayList<>();


    /** Adds tooltips with possible candidate values to empty editable cells. */
    private void handleAnalyze() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gridTextField[row][col].isDisabled())
                    continue;
                if (gridTextField[row][col].getText().trim().isEmpty()) {
                    // delegate possible-values to model
                    possibleValues = model.getPossibleValues(row, col);
                    gridTextField[row][col].setTooltip(new Tooltip("Possible: " + possibleToString(possibleValues)));
                } else {
                    gridTextField[row][col].setTooltip(null);
                }
            }
        }
    }

    /** Enables or disables the numeric entry buttons based on the current candidates. */
    private void enableDisableNumberButtons() {
        Button[] buttons = {buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine};

        for (int num = 0; num < 9; num++) {
            Button numberButton = buttons[num];
            if (numberButton != null && possibleValues != null) {
                if (possibleValues.contains(num + 1)) {
                    numberButton.setDisable(false);
                        numberButton.getStyleClass().remove("invalid-number"); // Ensure valid button style
                } else {
                    numberButton.setDisable(true);
                        if (!numberButton.getStyleClass().contains("invalid-number")) {
                            numberButton.getStyleClass().add("invalid-number"); // Add invalid style
                        }
                }
            }
        }

    }

    // Helper method to convert a collection of Integers to a comma-separated String for the tooltip
    private String possibleToString(Collection<Integer> col) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : col) {
            sb.append(i).append(", ");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length()); // Remove the trailing ", "
        }
        return sb.toString();
    }

    // Display the Sudoku grid
    public void DisplayGrid() {
        GridCell[][] cells = model.getCells();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                GridCell cell = cells[row][col];
                TextField tf = gridTextField[row][col];
                if (cell.isGiven()) {
                    tf.setText(String.valueOf(cell.getValue()));
                    StyleManager.markGiven(tf);
                } else if (cell.getValue() != 0) {
                    tf.setText(String.valueOf(cell.getValue()));
                    StyleManager.markEditable(tf);
                } else {
                    tf.setText("");
                    StyleManager.markEditable(tf);
                }
                tf.setTooltip(null);
            }
        }
        selectedTextField = null;
    }

    // Check if the user's solution is correct
    public boolean CheckSolution() {
        updatePlayerArray();
        if (!SolutionChecker.isComplete(player)) return false;
        int[][] expected = model.getSolutionGrid();
        if (expected != null && !SolutionChecker.equals(player, expected)) return false;
        return true;
    }
}
