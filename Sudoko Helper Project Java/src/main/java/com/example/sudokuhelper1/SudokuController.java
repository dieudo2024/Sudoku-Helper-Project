package com.example.sudokuhelper1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SudokuController {

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

    private TextField[][] gridTextField = new TextField[9][9];

    private int[][] currentGrid = new int[9][9];
    private int[][] player = new int[9][9];

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
    // Method to assign values from the TextField grid to the player array
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
    }
    @FXML
    private void initialize() {

        // Initialize the grid of TextFields
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField tf = new TextField();
                tf.setPrefWidth(40);
                tf.setPrefHeight(40);
                tf.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-background-color: white;");

                int finalRow = row;
                int finalCol = col;
                tf.setOnMouseClicked(e -> {
                    if (selectedTextField != null) {
                        selectedTextField.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-background-color: white;"); // Reset previous

                    }
                    selectedTextField = tf;
                    selectedTextField.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-background-color: lightblue; -fx-border-color: blue; -fx-border-width: 2;"); // Highlight selected
                    tf.requestFocus();
                    possibleValues = getPossibleValues(finalRow, finalCol);
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
                gridPane.add(tf, col, row);
            }
        }

        gridPane.setFocusTraversable(true); // Make the gridPane focusable for key events

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

    // Helper method to fill the selected cell with a value
    private void fillSelectedCell(int value) {
        if (selectedTextField != null && !selectedTextField.isDisabled()) {
            selectedTextField.setText(String.valueOf(value));
        }
        updatePlayerArray();
        enableDisableNumberButtons();
    }

    // Event handlers for number buttons
    public void onButtonOneClick(ActionEvent event) {
        fillSelectedCell(1);
    }

    public void handleButtonTwo(ActionEvent event) {
        fillSelectedCell(2);
    }

    public void handleButtonThree(ActionEvent event) {
        fillSelectedCell(3);
    }

    public void handleButtonFour(ActionEvent event) {
        fillSelectedCell(4);
    }

    public void handleButtonFive(ActionEvent event) {
        fillSelectedCell(5);
    }

    public void handleButtonSix(ActionEvent event) {
        fillSelectedCell(6);
    }

    public void handleButtonSeven(ActionEvent event) {
        fillSelectedCell(7);
    }

    public void handleButtonEight(ActionEvent event) {
        fillSelectedCell(8);
    }

    public void handleButtonNine(ActionEvent event) {
        fillSelectedCell(9);
    }

    public void onHandleEraseButtonClick(ActionEvent event) {
        handleErase();
    }

    public void onHandleSolveButton(ActionEvent event) {
        handleSolve();
    }

    public void onHandleAnalyzeButton(ActionEvent event) {
        handleAnalyze();
    }

    // Import Sudoku grid from a file
    public void onHandleImportButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Sudoku Grid");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt, *.csv)", "*.txt", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) importButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            HandleImport(selectedFile);
            DisplayGrid();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Import Canceled");
            alert.setHeaderText(null);
            alert.setContentText("File import was canceled.");
            alert.showAndWait();
        }
    }

    public void onHandleCheckSolutionButton(ActionEvent event) {
        handleCheckSolution();
    }

    // Erase the selected cell
    private void handleErase() {
        if (selectedTextField != null && !selectedTextField.isDisabled()) {
            selectedTextField.clear();
        }
        updatePlayerArray();
        enableDisableNumberButtons();
    }

    // Solve the Sudoku puzzle
    private void handleSolve() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                gridTextField[row][col].setText(String.valueOf(solution[row][col]));
                gridTextField[row][col].setDisable(true);
                gridTextField[row][col].setStyle("-fx-background-color: lightgreen; -fx-font-size: 18; -fx-alignment: center;");
            }
        }
    }

    // Check the user's solution
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


    // Analyze the Sudoku grid for possible values
    private void handleAnalyze() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gridTextField[row][col].isDisabled())
                    continue;
                if (gridTextField[row][col].getText().trim().isEmpty()) {
                    possibleValues = getPossibleValues(row, col).stream().map(Integer::intValue).toList();

                    // Display possible values as a tooltip
                    gridTextField[row][col].setTooltip(new Tooltip("Possible: " + possibleToString((Set<Integer>)possibleValues)));
                } else {
                    gridTextField[row][col].setTooltip(null);
                }
            }
        }
    }

    // Method to enable or disable number buttons based on the possible candidates
    private void enableDisableNumberButtons() {
        Button[] buttons = {buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine};

        for (int num = 0; num < 9; num++) {
            Button numberButton = buttons[num];
            if (numberButton != null && possibleValues != null) {
                if (possibleValues.contains(num + 1)) {
                    numberButton.setDisable(false);
                    numberButton.setStyle("");  // Reset button style
                } else {
                    numberButton.setDisable(true);
                    numberButton.setStyle("-fx-background-color: red;");  // Mark invalid numbers with red
                }
            }
        }

    }

    // Get possible values for a cell
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

    // Helper method to convert Set<Integer> to a comma-separated String for the tooltip
    private String possibleToString(Set<Integer> set) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : set) {
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
                int val = currentGrid[row][col];
                if (val != 0) {
                    gridTextField[row][col].setText(String.valueOf(val));
                    gridTextField[row][col].setDisable(true);
                    gridTextField[row][col].setStyle("-fx-background-color: lightgray; -fx-font-size: 18; -fx-alignment: center;");
                } else {
                    gridTextField[row][col].setText("");
                    gridTextField[row][col].setDisable(false);
                    gridTextField[row][col].setStyle("-fx-background-color: white; -fx-font-size: 18; -fx-alignment: center;");
                }
            }
        }
    }

    // Check if the user's solution is correct
    public boolean CheckSolution() {
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

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (player[row][col] != solution[row][col])
                    return false;
            }
        }
        return true;
    }

    // Import a Sudoku grid from a file
    public void HandleImport(File file) {
        if (file != null) { // Added null check here
            try (FileReader fileReader = new FileReader(file);
                 Scanner scanner = new Scanner(fileReader)) {
                scanner.useDelimiter(",|\\s+"); // Use comma or whitespace as delimiter

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