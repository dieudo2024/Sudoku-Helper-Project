package com.example.sudokuhelper.Model;

import java.util.ArrayList;
import java.util.List;

/** Utility that computes candidate values for an empty board cell. */
public class CandidateAnalyzer {

    /**
     * Computes valid candidates for the cell at ({@code row},{@code col}).
     * @param board 9x9 board
     * @param row row index
     * @param col column index
     * @return list of candidate digits (1..9). Returns an empty list when cell is non-empty.
     */
    public static List<Integer> analyze(int[][] board, int row, int col) {
        List<Integer> candidates = new ArrayList<>();
        if (board[row][col] != 0) return candidates;

        boolean[] used = new boolean[9];
        for (int c = 0; c < 9; c++) if (board[row][c] != 0) used[board[row][c] - 1] = true;
        for (int r = 0; r < 9; r++) if (board[r][col] != 0) used[board[r][col] - 1] = true;
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++)
            for (int c = boxCol; c < boxCol + 3; c++)
                if (board[r][c] != 0) used[board[r][c] - 1] = true;

        for (int n = 0; n < 9; n++) if (!used[n]) candidates.add(n + 1);
        return candidates;
    }
}
