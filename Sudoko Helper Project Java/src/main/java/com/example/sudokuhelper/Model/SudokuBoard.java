package com.example.sudokuhelper.Model;

/**
 * Static utility helpers for manipulating 9x9 Sudoku grids.
 */
public final class SudokuBoard {

    private static final int SIZE = 9;

    private SudokuBoard() {
        // to prevent instantiation
    }

    /** Creates a deep copy of the provided 9x9 grid. */
    public static int[][] deepCopy(int[][] src) {
        int[][] copy = new int[SIZE][SIZE];
        copyInto(src, copy);
        return copy;
    }

    /** Copies the content of {@code src} into {@code dest}. */
    public static void copyInto(int[][] src, int[][] dest) {
        for (int i = 0; i < SIZE; i++) System.arraycopy(src[i], 0, dest[i], 0, SIZE);
    }
}
