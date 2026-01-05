package com.example.sudokuhelper.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates solvable Sudoku puzzles using backtracking and randomization.
 */
public final class SudokuGenerator {

    private static final Random RANDOM = new Random();

    private SudokuGenerator() {
        // utility
    }

    /**
     * Generates a Sudoku puzzle with a unique solution.
     * @param clues desired number of given cells (minimum 17)
     * @return 9x9 puzzle matrix with zeros for empty cells
     */
    public static int[][] generate(int clues) {
        int[][] full = new int[9][9];
        fillGrid(full, 0, 0);

        int[][] puzzle = SudokuBoard.deepCopy(full);
        removeCells(puzzle, 81 - Math.max(17, Math.min(clues, 81)));
        return puzzle;
    }

    private static boolean fillGrid(int[][] grid, int row, int col) {
        if (row == 9) return true;
        int nextRow = col == 8 ? row + 1 : row;
        int nextCol = col == 8 ? 0 : col + 1;

        List<Integer> numbers = new ArrayList<>();
        for (int n = 1; n <= 9; n++) numbers.add(n);
        Collections.shuffle(numbers, RANDOM);

        for (int number : numbers) {
            if (isSafe(grid, row, col, number)) {
                grid[row][col] = number;
                if (fillGrid(grid, nextRow, nextCol)) return true;
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private static void removeCells(int[][] grid, int removals) {
        List<int[]> positions = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                positions.add(new int[]{row, col});
            }
        }
        Collections.shuffle(positions, RANDOM);

        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= removals) break;
            int row = pos[0];
            int col = pos[1];
            int backup = grid[row][col];
            grid[row][col] = 0;

            int[][] copy = SudokuBoard.deepCopy(grid);
            if (!hasUniqueSolution(copy)) {
                grid[row][col] = backup;
            } else {
                removed++;
            }
        }
    }

    private static boolean hasUniqueSolution(int[][] grid) {
        return countSolutions(grid, 0, 0, 0) == 1;
    }

    private static int countSolutions(int[][] grid, int row, int col, int count) {
        if (count > 1) return count;
        if (row == 9) return count + 1;
        int nextRow = col == 8 ? row + 1 : row;
        int nextCol = col == 8 ? 0 : col + 1;

        if (grid[row][col] != 0) return countSolutions(grid, nextRow, nextCol, count);

        for (int value = 1; value <= 9; value++) {
            if (isSafe(grid, row, col, value)) {
                grid[row][col] = value;
                count = countSolutions(grid, nextRow, nextCol, count);
                if (count > 1) break;
            }
        }
        grid[row][col] = 0;
        return count;
    }

    private static boolean isSafe(int[][] grid, int row, int col, int number) {
        for (int c = 0; c < 9; c++) if (grid[row][c] == number) return false;
        for (int r = 0; r < 9; r++) if (grid[r][col] == number) return false;

        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (grid[r][c] == number) return false;
            }
        }
        return true;
    }
}
