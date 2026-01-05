package com.example.sudokuhelper.Model;

/**
 * Immutable container for serialized Sudoku session data.
 */
public final class SessionData {

    private final int[][] current;
    private final int[][] player;

    public SessionData(int[][] current, int[][] player) {
        this.current = SudokuBoard.deepCopy(current);
        this.player = SudokuBoard.deepCopy(player);
    }

    public int[][] getCurrent() {
        return SudokuBoard.deepCopy(current);
    }

    public int[][] getPlayer() {
        return SudokuBoard.deepCopy(player);
    }
}
