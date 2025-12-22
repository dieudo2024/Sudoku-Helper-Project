package com.example.sudokuhelper.Model;

/**
 * Lightweight wrapper for a 9x9 Sudoku board.
 * Provides convenient accessors and defensive copies.
 */
public class SudokuBoard {

    private static final int SIZE = 9;

    private final int[][] cells = new int[SIZE][SIZE];

    /** Constructs an empty board (all zeros). */
    public SudokuBoard() {}

    /** Constructs a board by copying the provided 9x9 array.
     * @param src source array
     */
    public SudokuBoard(int[][] src) {
        copyInto(src, cells);
    }

    /** Returns the value at the given cell (0 if empty).
     * @param row row index
     * @param col column index
     * @return cell value
     */
    public int get(int row, int col) {
        return cells[row][col];
    }

    /** Sets the value at the given cell.
     * @param row row index
     * @param col column index
     * @param value value to set (0 for empty)
     */
    public void set(int row, int col, int value) {
        cells[row][col] = value;
    }

    /**
     * Returns a defensive copy of the board as a 9x9 array.
     * @return copy of the board
     */
    public int[][] toArray() {
        return deepCopy(cells);
    }

    /** Returns a deep copy of this board. */
    public SudokuBoard copy() {
        return new SudokuBoard(toArray());
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
