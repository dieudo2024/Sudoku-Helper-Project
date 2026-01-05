package com.example.sudokuhelper.Model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Model class that holds the current and player Sudoku grids and provides
 * operations such as solving, candidate computation and file import.
 */
public class SudokuModel {

    private final int[][] currentGrid = new int[9][9];
    private final int[][] player = new int[9][9];
    private final int[][] solution = new int[9][9];
    private boolean solutionAvailable = false;

    /**
     * Returns a defensive copy of the current grid (the given puzzle).
     * @return a 9x9 int matrix copy of the current grid
     */
    public int[][] getCurrentGrid() {
        return SudokuBoard.deepCopy(currentGrid);
    }

    /**
     * Returns a defensive copy of the player's grid (current entries).
     * @return a 9x9 int matrix copy of the player grid
     */
    public int[][] getPlayerGrid() {
        return SudokuBoard.deepCopy(player);
    }

    /**
     * Replaces the player grid content with a copy of {@code src}.
     * @param src 9x9 source matrix
     */
    public void setPlayerGrid(int[][] src) {
        SudokuBoard.copyInto(src, player);
    }

    /**
     * Sets the current (given) grid from {@code src}.
     * @param src 9x9 source matrix
     */
    public void setCurrentGrid(int[][] src) {
        SudokuBoard.copyInto(src, currentGrid);
        refreshSolution();
    }

    /**
     * Returns a defensive copy of the solved grid when available.
     * @return solved 9x9 matrix, or {@code null} if no solution was computed
     */
    public int[][] getSolutionGrid() {
        return solutionAvailable ? SudokuBoard.deepCopy(solution) : null;
    }

    /**
     * Attempts to solve the player grid in-place. Empty cells must be 0.
     * @return {@code true} if the grid was solved; {@code false} otherwise
     */
    public boolean solve() {
        boolean solved = SudokuSolver.solve(player);
        if (solved) {
            SudokuBoard.copyInto(player, solution);
            solutionAvailable = true;
        }
        return solved;
    }

    /**
     * Returns a list of candidate values that can be placed at ({@code row},{@code col})
     * based on the current player grid.
     * @param row target row (0-8)
     * @param col target column (0-8)
     * @return list of integers in range 1..9 that are valid candidates
     */
    public List<Integer> getPossibleValues(int row, int col) {
        return CandidateAnalyzer.analyze(player, row, col);
    }

    /**
     * Finds a human-style hint for the current player grid.
     * @return optional hint describing the next logical move
     */
    public Optional<Hint> computeHint() {
        return HintGenerator.findHint(player);
    }

    /**
     * Generates a new random puzzle and loads it into current and player grids.
     * @param clues number of givens to keep (17-81)
     */
    public void generateRandomPuzzle(int clues) {
        int[][] puzzle = SudokuGenerator.generate(clues);
        setCurrentGrid(puzzle);
        setPlayerGrid(puzzle);
    }

    /**
     * Imports a puzzle from a text or CSV file into the current and player grids.
     * @param file input file
     * @return {@code true} if the import succeeded and the grids were filled; {@code false} otherwise
     * @throws IOException when file reading fails
     */
    public boolean importFromFile(File file) throws IOException {
        if (file == null) return false;
        int[][] imported = FileImporter.importFromFile(file);
        setCurrentGrid(imported);
        setPlayerGrid(imported);
        return true;
    }

    /**
     * Provides a snapshot of the player grid paired with given flags from the original puzzle.
     * @return 9x9 array describing each cell's value and whether it is a fixed clue
     */
    public GridCell[][] getCells() {
        GridCell[][] cells = new GridCell[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                boolean given = currentGrid[r][c] != 0;
                int value = given ? currentGrid[r][c] : player[r][c];
                cells[r][c] = new GridCell(value, given);
            }
        }
        return cells;
    }

    /**
     * Recomputes the solved grid from the current givens.
     */
    private void refreshSolution() {
        int[][] snapshot = SudokuBoard.deepCopy(currentGrid);
        if (SudokuSolver.solve(snapshot)) {
            SudokuBoard.copyInto(snapshot, solution);
            solutionAvailable = true;
        } else {
            solutionAvailable = false;
        }
    }

}
