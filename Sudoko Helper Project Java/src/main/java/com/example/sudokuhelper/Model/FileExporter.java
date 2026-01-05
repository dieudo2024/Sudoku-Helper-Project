package com.example.sudokuhelper.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility for exporting Sudoku grids to a text file compatible with the importer.
 */
public final class FileExporter {

    private FileExporter() {
        // utility class
    }

    /**
     * Writes the supplied grid as 9 lines of comma-separated digits.
     * Empty cells are exported as 0.
     *
     * @param grid  9x9 grid to write
     * @param file  output file destination
     * @throws IOException when writing fails
     */
    public static void exportGrid(int[][] grid, File file) throws IOException {
        if (grid == null || grid.length != 9) throw new IllegalArgumentException("Grid must be 9x9");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    writer.print(grid[row][col]);
                    if (col < 8) writer.print(',');
                }
                writer.println();
            }
        }
    }
}
