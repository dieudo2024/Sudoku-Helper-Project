package com.example.sudokuhelper.Controller;

/**
 * JavaFX controller for the Sudoku UI.
 * <p>Handles user interactions (button clicks, import, analyze, solve) and
 * delegates game logic to {@link com.example.sudokuhelper.Model.SudokuModel}.
 */

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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

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
    private Button eraseButton;
    @FXML
    private Button solveButton;
    @FXML
    private Button analyzeButton;
    @FXML
    private Button importButton;
    @FXML
    private Button checkSolutionButton;

    @FXML
    private GridPane gridPane;

    private final TextField[][] gridTextField = new TextField[9][9];

    private final int[][] player = new int[9][9];

    private final int[][] solution = {
            { 7, 9, 2, 3, 5, 1, 8, 6, 4 },
            { 3, 8, 1, 4, 6, 9, 5, 2, 7 },
            { 4, 5, 9, 2, 8, 7, 1, 6, 3 },
            { 1, 4, 5, 6, 9, 2, 7, 3, 8 },
            { 6, 7, 8, 5, 1, 3, 2, 4, 9 },
            { 2, 3, 9, 8, 7, 4, 6, 5, 1 },
            { 5, 1, 7, 9, 2, 6, 3, 8, 4 },
            { 9, 6, 3, 1, 4, 8, 2, 7, 5 },
            { 8, 2, 4, 7, 3, 5, 9, 1, 6 }
    };

    private TextField selectedTextField = null;
    /**
     * Reads the current values from the TextField grid into the internal player array
     * and synchronizes the model's player grid.
     */
    private void updatePlayerArray() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = gridTextField[row][col].getText().trim();
                if (text.isEmpty()) {
                    player[row][col] = 0; // Empty cells are represented as 0
                } else {
                    try {
                        player[row][col] = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        // Handle invalid input gracefully, e.g. show an alert
                        player[row][col] = 0; // Reset invalid input to 0
                    }
                }
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
    @FXML
    /**
     * JavaFX initialization hook. Builds the 9x9 TextField grid, attaches listeners
     * and configures keyboard handling.
     */
    private void initialize() {

        // Initialize the grid of TextFields
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField tf = new TextField();
                tf.setPrefWidth(40);
                tf.setPrefHeight(40);
                // Use CSS classes (sudoku.css) for styling
                tf.getStyleClass().add("cell");

                int finalRow = row;
                int finalCol = col;
                tf.setOnMouseClicked(e -> {
                    if (selectedTextField != null) {
                        selectedTextField.getStyleClass().remove("cell-selected"); // Reset previous selected
                    }
                    selectedTextField = tf;
                    if (!selectedTextField.getStyleClass().contains("cell-selected"))
                        selectedTextField.getStyleClass().add("cell-selected"); // Highlight selected
                    // highlight row/column/box
                    highlightRelatedCells(finalRow, finalCol);
                    tf.requestFocus();
                    possibleValues = model.getPossibleValues(finalRow, finalCol);
                    enableDisableNumberButtons();
                    updatePlayerArray();
                });

                // Add listener to validate input
                tf.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Check if the new value is not empty and does not match digits 1-9
                    if (!newValue.isEmpty() && !newValue.matches("[1-9]")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Invalid Input");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter numbers from 1 to 9 only.");
                        alert.showAndWait();
                        tf.setText(oldValue); // Revert to the previous valid input
                    } else if (newValue.length() > 1) {
                        tf.setText(newValue.substring(0, 1)); // Limit input to a single digit
                    }
                });

                gridTextField[row][col] = tf;
                // add box border classes for 3x3 separation
                if (row % 3 == 0) tf.getStyleClass().add("box-border-top");
                if (row % 3 == 2) tf.getStyleClass().add("box-border-bottom");
                if (col % 3 == 0) tf.getStyleClass().add("box-border-left");
                if (col % 3 == 2) tf.getStyleClass().add("box-border-right");
                gridPane.add(tf, col, row);
            }
        }

        gridPane.setFocusTraversable(true); // Make the gridPane focusable for key events
        if (!gridPane.getStyleClass().contains("sudoku-grid")) gridPane.getStyleClass().add("sudoku-grid");

        // Add style class to numeric buttons for CSS toggling
        Button[] numButtons = {buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine};
        for (Button b : numButtons) if (b != null && !b.getStyleClass().contains("num-button")) b.getStyleClass().add("num-button");

        // Handle key presses on the grid
        gridPane.setOnKeyPressed(event -> {
            if (selectedTextField != null && !selectedTextField.isDisabled()) {
                String key = event.getText();
                if (key.matches("[1-9]")) {
                    selectedTextField.setText(key); // Update text if it's a digit
                     // To disable impossible moves
                } else if (event.getCode().toString().equals("BACK_SPACE") || event.getCode().toString().equals("DELETE")) {
                    selectedTextField.clear(); // Clear text on backspace or delete
                }
            }
        });
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
    public void onButtonOneClick(ActionEvent event) {
        fillSelectedCell(1);
    }
    /** Inserts digit 2 into the selected cell. */
    public void handleButtonTwo(ActionEvent event) {
        fillSelectedCell(2);
    }
    /** Inserts digit 3 into the selected cell. */
    public void handleButtonThree(ActionEvent event) {
        fillSelectedCell(3);
    }
    /** Inserts digit 4 into the selected cell. */
    public void handleButtonFour(ActionEvent event) {
        fillSelectedCell(4);
    }
    /** Inserts digit 5 into the selected cell. */
    public void handleButtonFive(ActionEvent event) {
        fillSelectedCell(5);
    }
    /** Inserts digit 6 into the selected cell. */
    public void handleButtonSix(ActionEvent event) {
        fillSelectedCell(6);
    }
    /** Inserts digit 7 into the selected cell. */
    public void handleButtonSeven(ActionEvent event) {
        fillSelectedCell(7);
    }
    /** Inserts digit 8 into the selected cell. */
    public void handleButtonEight(ActionEvent event) {
        fillSelectedCell(8);
    }
    /** Inserts digit 9 into the selected cell. */
    public void handleButtonNine(ActionEvent event) {
        fillSelectedCell(9);
    }
    /** Erases the currently selected cell. */
    public void onHandleEraseButtonClick(ActionEvent event) {
        handleErase();
    }
    /** Trigger the solver for the current puzzle. */
    public void onHandleSolveButton(ActionEvent event) {
        handleSolve();
    }
    /** Show candidate values as tooltips for empty cells. */
    public void onHandleAnalyzeButton(ActionEvent event) {
        handleAnalyze();
    }

    /** Opens a file chooser and imports a Sudoku puzzle from a selected file. */
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
        if (!solved) {
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
                gridTextField[row][col].setText(String.valueOf(solvedBoard[row][col]));
                TextField tf = gridTextField[row][col];
                tf.setDisable(true);
                tf.getStyleClass().removeAll("cell", "cell-given", "cell-selected");
                if (!tf.getStyleClass().contains("cell-solved")) tf.getStyleClass().add("cell-solved");
            }
        }
    }

    /** Evaluates the player's solution and shows an alert with the result. */
    private void handleCheckSolution() {
        boolean solved = CheckSolution();
        Alert alert = new Alert(solved ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle("Sudoku Result");
        alert.setHeaderText(null);
        alert.setContentText(solved ? "Congratulations! You solved the sudoku correctly." : "The solution is incorrect or incomplete. Keep trying!");
        alert.showAndWait();
    }

    //Set<Integer> possible;
    List<Integer> possibleValues = new ArrayList<>();

    private int[][] currentGrid;


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

    /**
     * Returns the list of possible digits for the given cell using the controller's
     * player grid. This mirrors the model's candidate logic for controller-side checks.
     */
    private List<Integer> getPossibleValues(int i, int j) {
        boolean[] possible = new boolean[9];
        int[][] board = player;
        // Eliminate numbers in the same row
        for (int col = 0; col < 9; col++) {
            if (board[i][col] != 0) {
                possible[board[i][col] - 1] = true;
            }
        }
        // Eliminate numbers in the same column
        for (int row = 0; row < 9; row++) {
            if (board[row][j] != 0) {
                possible[board[row][j] - 1] = true;
            }
        }

        // Eliminate numbers in the same 3x3 subgrid
        int boxRowStart = (i / 3) * 3;
        int boxColStart = (j / 3) * 3;
        for (int row = boxRowStart; row < boxRowStart + 3; row++) {
            for (int col = boxColStart; col < boxColStart + 3; col++) {
                if (board[row][col] != 0) {
                    possible[board[row][col] - 1] = true;
                }
            }
        }
        List<Integer> candidates = new ArrayList<>();
        for (int num = 0; num < 9; num++) {
            if (!possible[num]) {
                candidates.add(num + 1);
            }
        }
        return candidates;
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
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int val = model.getCurrentGrid()[row][col];
                TextField tf = gridTextField[row][col];
                if (val != 0) {
                    tf.setText(String.valueOf(val));
                    tf.setDisable(true);
                    tf.getStyleClass().removeAll("cell", "cell-solved", "cell-selected");
                    if (!tf.getStyleClass().contains("cell-given")) tf.getStyleClass().add("cell-given");
                } else {
                    tf.setText("");
                    tf.setDisable(false);
                    tf.getStyleClass().removeAll("cell-given", "cell-solved", "cell-selected");
                    if (!tf.getStyleClass().contains("cell")) tf.getStyleClass().add("cell");
                }
            }
        }
    }

    // Check if the user's solution is correct
    public boolean CheckSolution() {
        // update player array from UI then delegate check to model
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = gridTextField[row][col].getText().trim();
                if (text.isEmpty())
                    return false;
                try {
                    player[row][col] = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        model.setPlayerGrid(player);
        return model.checkSolution(solution);
    }

    // Import a Sudoku grid from a file
    public void HandleImport(File file) {
        if (file != null) { // Added null check here
            try (FileReader fileReader = new FileReader(file);
                 Scanner scanner = new Scanner(fileReader)) {
                scanner.useDelimiter(",|\s+"); // Use comma or whitespace as delimiter

                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (scanner.hasNext()) {
                            String value = scanner.next().trim();
                            try {
                                if (!value.isEmpty()) {
                                    int val = Integer.parseInt(value);
                                    currentGrid[row][col] = val;
                                    player[row][col] = val;
                                } else {
                                    currentGrid[row][col] = 0;
                                    player[row][col] = 0;
                                }
                            } catch (NumberFormatException e) {
                                currentGrid[row][col] = 0;
                                player[row][col] = 0;
                            }
                        } else {
                            return;
                        }
                    }
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    } else if (row < 8) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error importing file: " + e.getMessage());
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Import Error");
                alert.setHeaderText(null);
                alert.setContentText("Error reading the file: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
