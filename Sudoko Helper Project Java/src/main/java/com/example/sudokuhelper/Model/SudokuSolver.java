package com.example.sudokuhelper.Model;

/**
 * Backtracking Sudoku solver utility.
 * <p>Provides a simple recursive solver that fills the provided 9x9 board
 * in-place. Empty cells are represented by 0.</p>
 */
public class SudokuSolver {

    /**
     * Attempts to solve the provided Sudoku board using backtracking.
     * @param board a 9x9 int matrix where 0 represents an empty cell
     * @return {@code true} when a solution was found and the board is filled; {@code false} otherwise
     */
    public static boolean solve(int[][] board) {
        int[] pos = findUnassigned(board);
        if (pos == null) {
            return true; // solved
        }
        int row = pos[0], col = pos[1];

        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (solve(board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false; // trigger backtracking
    }

    /**
     * Finds an unassigned (empty) cell in the board.
     * @param board the board to search
     * @return an array of two integers {row, col} for the first empty cell, or {@code null} if none
     */
    private static int[] findUnassigned(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    /**
     * Checks whether it is safe to place {@code num} at position ({@code row},{@code col}).
     * @param board the Sudoku board
     * @param row target row
     * @param col target column
     * @param num candidate number (1-9)
     * @return {@code true} if placing {@code num} does not violate Sudoku constraints
     */
    private static boolean isSafe(int[][] board, int row, int col, int num) {
        // check row and column
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) return false;
            if (board[i][col] == num) return false;
        }
        // check 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (board[r][c] == num) return false;
            }
        }
        return true;
    }
}
