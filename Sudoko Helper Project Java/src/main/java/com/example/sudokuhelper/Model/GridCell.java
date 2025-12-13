package com.example.sudokuhelper.Model;

/**
 * Simple data holder representing a Sudoku cell and whether it is a given.
 */
public class GridCell {
    private int value;
    private boolean given;

    /**
     * @param value initial value (0 for empty)
     * @param given whether the cell is a given (original puzzle value)
     */
    public GridCell(int value, boolean given) {
        this.value = value;
        this.given = given;
    }

    /** Returns the cell value (0 if empty). */
    public int getValue() {
        return value;
    }

    /** Sets the cell value. */
    public void setValue(int value) {
        this.value = value;
    }

    /** Returns true when this cell is a given (not editable). */
    public boolean isGiven() {
        return given;
    }

    /** Sets the given flag for this cell. */
    public void setGiven(boolean given) {
        this.given = given;
    }
}
