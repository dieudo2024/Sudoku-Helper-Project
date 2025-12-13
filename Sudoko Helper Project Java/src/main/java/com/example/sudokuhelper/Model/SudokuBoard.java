package com.example.sudokuhelper.Model;

/**
 * Lightweight wrapper for a 9x9 Sudoku board.
 * Provides convenient accessors and defensive copies.
 */
public class SudokuBoard {

    private final int[][] cells = new int[9][9];

    /** Constructs an empty board (all zeros). */
    public SudokuBoard() {}

    /** Constructs a board by copying the provided 9x9 array.
     * @param src source array
     */
    public SudokuBoard(int[][] src) {
        for (int i = 0; i < 9; i++) System.arraycopy(src[i], 0, cells[i], 0, 9);
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
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(cells[i], 0, copy[i], 0, 9);
        return copy;
    }

    /** Returns a deep copy of this board. */
    public SudokuBoard copy() {
        return new SudokuBoard(toArray());
    }
}
