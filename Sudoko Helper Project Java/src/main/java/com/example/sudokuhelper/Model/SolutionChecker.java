package com.example.sudokuhelper.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for validating and comparing Sudoku solutions.
 */
public class SolutionChecker {

    /**
     * Checks if the board contains only digits 1..9 (no empties).
     * @param board 9x9 board
     * @return {@code true} if complete
     */
    public static boolean isComplete(int[][] board) {
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) if (board[r][c] < 1 || board[r][c] > 9) return false;
        return true;
    }

    /**
     * Validates whether the board is a correct Sudoku solution.
     * @param board 9x9 board
     * @return {@code true} when rows, columns and 3x3 boxes contain digits 1..9 exactly once
     */
    public static boolean isValidSolution(int[][] board) {
        // check rows
        for (int r = 0; r < 9; r++) {
            Set<Integer> seen = new HashSet<>();
            for (int c = 0; c < 9; c++) {
                int v = board[r][c];
                if (v < 1 || v > 9 || !seen.add(v)) return false;
            }
        }
        // check cols
        for (int c = 0; c < 9; c++) {
            Set<Integer> seen = new HashSet<>();
            for (int r = 0; r < 9; r++) {
                int v = board[r][c];
                if (v < 1 || v > 9 || !seen.add(v)) return false;
            }
        }
        // check boxes
        for (int br = 0; br < 3; br++) for (int bc = 0; bc < 3; bc++) {
            Set<Integer> seen = new HashSet<>();
            for (int r = br * 3; r < br * 3 + 3; r++)
                for (int c = bc * 3; c < bc * 3 + 3; c++) {
                    int v = board[r][c];
                    if (v < 1 || v > 9 || !seen.add(v)) return false;
                }
        }
        return true;
    }

    /**
     * Compares two boards for equality.
     * @param a first board
     * @param b second board
     * @return {@code true} if all corresponding cells are equal
     */
    public static boolean equals(int[][] a, int[][] b) {
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) if (a[r][c] != b[r][c]) return false;
        return true;
    }
}
