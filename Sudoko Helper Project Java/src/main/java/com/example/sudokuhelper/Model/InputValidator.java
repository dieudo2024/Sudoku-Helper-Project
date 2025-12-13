package com.example.sudokuhelper.Model;

/**
 * Small validation utilities for user input.
 */
public class InputValidator {

    /**
     * Returns {@code true} when the supplied string represents a single digit 1..9.
     * @param s input string
     * @return {@code true} if valid
     */
    public static boolean isValidDigit(String s) {
        return s != null && s.matches("[1-9]");
    }
}
