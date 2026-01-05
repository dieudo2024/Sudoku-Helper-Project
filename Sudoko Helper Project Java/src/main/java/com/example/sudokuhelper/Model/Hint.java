package com.example.sudokuhelper.Model;

/**
 * Represents a simple Sudoku hint derived from board analysis.
 */
public class Hint {

    public enum HintType {
        NAKED_SINGLE,
        HIDDEN_SINGLE_ROW,
        HIDDEN_SINGLE_COLUMN,
        HIDDEN_SINGLE_BOX
    }

    private final HintType type;
    private final int row;
    private final int col;
    private final int value;
    private final String explanation;

    public Hint(HintType type, int row, int col, int value, String explanation) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.value = value;
        this.explanation = explanation;
    }

    public HintType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public String getExplanation() {
        return explanation;
    }
}
