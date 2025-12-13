package com.example.sudokuhelper.Model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Helper to import a Sudoku grid from a text or CSV file.
 */
public class FileImporter {

    /**
     * Parses a file and returns a 9x9 grid. Missing or invalid tokens become 0.
     * @param file input file
     * @return a 9x9 int grid
     * @throws IOException on I/O or parsing errors
     */
    public static int[][] importFromFile(File file) throws IOException {
        int[][] grid = new int[9][9];
        try (FileReader fr = new FileReader(file); Scanner scanner = new Scanner(fr)) {
            scanner.useDelimiter(",|\\s+");
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (scanner.hasNext()) {
                        String s = scanner.next().trim();
                        try {
                            if (!s.isEmpty()) grid[r][c] = Integer.parseInt(s);
                            else grid[r][c] = 0;
                        } catch (NumberFormatException ex) {
                            grid[r][c] = 0;
                        }
                    } else {
                        throw new IOException("Not enough tokens in file");
                    }
                }
                if (scanner.hasNextLine()) scanner.nextLine();
            }
        }
        return grid;
    }
}
