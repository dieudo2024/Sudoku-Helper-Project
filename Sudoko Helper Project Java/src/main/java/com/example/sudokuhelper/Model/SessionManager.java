package com.example.sudokuhelper.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Handles persistence of Sudoku play sessions (current puzzle and player entries).
 */
public final class SessionManager {

    private SessionManager() {
        // utility
    }

    public static void saveSession(File file, int[][] current, int[][] player) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("#current");
            writeGrid(writer, current);
            writer.println("#player");
            writeGrid(writer, player);
        }
    }

    private static void writeGrid(PrintWriter writer, int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                writer.print(grid[row][col]);
                if (col < 8) writer.print(',');
            }
            writer.println();
        }
    }

    public static SessionData loadSession(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int[][] current = new int[9][9];
            int[][] player = new int[9][9];
            String section = null;
            String line;
            int rowIndex = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("#")) {
                    section = line.substring(1).toLowerCase();
                    rowIndex = 0;
                    continue;
                }
                if (section == null) continue;
                int[][] target = switch (section) {
                    case "current" -> current;
                    case "player" -> player;
                    default -> null;
                };
                if (target == null || rowIndex >= 9) continue;
                StringTokenizer tokenizer = new StringTokenizer(line, ", ");
                int col = 0;
                while (tokenizer.hasMoreTokens() && col < 9) {
                    String token = tokenizer.nextToken();
                    try {
                        target[rowIndex][col] = Integer.parseInt(token);
                    } catch (NumberFormatException ex) {
                        target[rowIndex][col] = 0;
                    }
                    col++;
                }
                rowIndex++;
            }
            return new SessionData(current, player);
        }
    }
}
