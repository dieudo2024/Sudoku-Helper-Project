package com.example.sudokuhelper.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.sudokuhelper.Model.Hint.HintType;

/**
 * Derives simple Sudoku hints (singles in cells, rows, columns, or boxes).
 */
public final class HintGenerator {

    private HintGenerator() {
        // utility
    }

    public static Optional<Hint> findHint(int[][] board) {
        Hint naked = findNakedSingle(board);
        if (naked != null) return Optional.of(naked);

        Hint rowHidden = findHiddenSingleRow(board);
        if (rowHidden != null) return Optional.of(rowHidden);

        Hint colHidden = findHiddenSingleColumn(board);
        if (colHidden != null) return Optional.of(colHidden);

        Hint boxHidden = findHiddenSingleBox(board);
        if (boxHidden != null) return Optional.of(boxHidden);

        return Optional.empty();
    }

    private static Hint findNakedSingle(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != 0) continue;
                List<Integer> candidates = CandidateAnalyzer.analyze(board, row, col);
                if (candidates.size() == 1) {
                    int value = candidates.get(0);
                    String explanation = "Cell (" + (row + 1) + ", " + (col + 1) + ") only allows " + value + ".";
                    return new Hint(HintType.NAKED_SINGLE, row, col, value, explanation);
                }
            }
        }
        return null;
    }

    private static Hint findHiddenSingleRow(int[][] board) {
        for (int row = 0; row < 9; row++) {
            int[] counts = new int[9];
            List<List<Integer>> rowCandidates = new ArrayList<>(9);
            for (int col = 0; col < 9; col++) {
                List<Integer> candidates = board[row][col] == 0 ? CandidateAnalyzer.analyze(board, row, col) : List.of();
                rowCandidates.add(candidates);
                for (int value : candidates) counts[value - 1]++;
            }
            for (int value = 1; value <= 9; value++) {
                if (counts[value - 1] == 1) {
                    for (int col = 0; col < 9; col++) {
                        List<Integer> candidates = rowCandidates.get(col);
                        if (candidates.contains(value)) {
                            String explanation = "In row " + (row + 1) + ", only column " + (col + 1) + " can take " + value + ".";
                            return new Hint(HintType.HIDDEN_SINGLE_ROW, row, col, value, explanation);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Hint findHiddenSingleColumn(int[][] board) {
        for (int col = 0; col < 9; col++) {
            int[] counts = new int[9];
            List<List<Integer>> columnCandidates = new ArrayList<>(9);
            for (int row = 0; row < 9; row++) {
                List<Integer> candidates = board[row][col] == 0 ? CandidateAnalyzer.analyze(board, row, col) : List.of();
                columnCandidates.add(candidates);
                for (int value : candidates) counts[value - 1]++;
            }
            for (int value = 1; value <= 9; value++) {
                if (counts[value - 1] == 1) {
                    for (int row = 0; row < 9; row++) {
                        List<Integer> candidates = columnCandidates.get(row);
                        if (candidates.contains(value)) {
                            String explanation = "In column " + (col + 1) + ", only row " + (row + 1) + " can take " + value + ".";
                            return new Hint(HintType.HIDDEN_SINGLE_COLUMN, row, col, value, explanation);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Hint findHiddenSingleBox(int[][] board) {
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                int[] counts = new int[9];
                List<List<Integer>> boxCandidates = new ArrayList<>(9);
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        int row = boxRow * 3 + r;
                        int col = boxCol * 3 + c;
                        List<Integer> candidates = board[row][col] == 0 ? CandidateAnalyzer.analyze(board, row, col) : List.of();
                        boxCandidates.add(candidates);
                        for (int value : candidates) counts[value - 1]++;
                    }
                }
                for (int value = 1; value <= 9; value++) {
                    if (counts[value - 1] == 1) {
                        for (int index = 0; index < boxCandidates.size(); index++) {
                            List<Integer> candidates = boxCandidates.get(index);
                            if (candidates.contains(value)) {
                                int row = boxRow * 3 + index / 3;
                                int col = boxCol * 3 + index % 3;
                                String explanation = "In box (" + (boxRow + 1) + ", " + (boxCol + 1) + ") only cell (" + (row + 1) + ", " + (col + 1) + ") fits " + value + ".";
                                return new Hint(HintType.HIDDEN_SINGLE_BOX, row, col, value, explanation);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
