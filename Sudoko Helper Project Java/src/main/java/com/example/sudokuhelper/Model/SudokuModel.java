package com.example.sudokuhelper.Model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Model class that holds the current and player Sudoku grids and provides
 * operations such as solving, candidate computation and file import.
 */
public class SudokuModel {

    private int[][] currentGrid = new int[9][9];
    private int[][] player = new int[9][9];

    /**
     * Returns a defensive copy of the current grid (the given puzzle).
     * @return a 9x9 int matrix copy of the current grid
     */
    public int[][] getCurrentGrid() {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(currentGrid[i], 0, copy[i], 0, 9);
        return copy;
    }

    /**
     * Returns a defensive copy of the player's grid (current entries).
     * @return a 9x9 int matrix copy of the player grid
     */
    public int[][] getPlayerGrid() {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(player[i], 0, copy[i], 0, 9);
        return copy;
    }

    /**
     * Replaces the player grid content with a copy of {@code src}.
     * @param src 9x9 source matrix
     */
    public void setPlayerGrid(int[][] src) {
        for (int i = 0; i < 9; i++) System.arraycopy(src[i], 0, player[i], 0, 9);
    }

    /**
     * Sets the current (given) grid from {@code src}.
     * @param src 9x9 source matrix
     */
    public void setCurrentGrid(int[][] src) {
        for (int i = 0; i < 9; i++) System.arraycopy(src[i], 0, currentGrid[i], 0, 9);
    }

    /**
     * Attempts to solve the player grid in-place. Empty cells must be 0.
     * @return {@code true} if the grid was solved; {@code false} otherwise
     */
    public boolean solve() {
        // solve player grid in-place (treat empty cells as 0)
        return SudokuSolver.solve(player);
    }

    /**
     * Returns a list of candidate values that can be placed at ({@code row},{@code col})
     * based on the current player grid.
     * @param row target row (0-8)
     * @param col target column (0-8)
     * @return list of integers in range 1..9 that are valid candidates
     */
    public List<Integer> getPossibleValues(int row, int col) {
        boolean[] used = new boolean[9];
        for (int c = 0; c < 9; c++) if (player[row][c] != 0) used[player[row][c] - 1] = true;
        for (int r = 0; r < 9; r++) if (player[r][col] != 0) used[player[r][col] - 1] = true;
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++)
            for (int c = boxCol; c < boxCol + 3; c++)
                if (player[r][c] != 0) used[player[r][c] - 1] = true;
        List<Integer> candidates = new ArrayList<>();
        for (int n = 0; n < 9; n++) if (!used[n]) candidates.add(n + 1);
        return candidates;
    }

    /**
     * Imports a puzzle from a text or CSV file into the current and player grids.
     * @param file input file
     * @return {@code true} if the import succeeded and the grids were filled; {@code false} otherwise
     * @throws IOException when file reading fails
     */
    public boolean importFromFile(File file) throws IOException {
        if (file == null) return false;
        try (FileReader fr = new FileReader(file); Scanner scanner = new Scanner(fr)) {
            scanner.useDelimiter(",|\\s+");
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
                        return false;
                    }
                }
                if (scanner.hasNextLine()) scanner.nextLine();
            }
        }
        return true;
    }

    /**
     * Compares the player's grid to an expected solution.
     * @param expected a 9x9 expected solution grid
     * @return {@code true} if the player's entries match {@code expected}
     */
    public boolean checkSolution(int[][] expected) {
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) if (player[r][c] != expected[r][c]) return false;
        return true;
    }
}
