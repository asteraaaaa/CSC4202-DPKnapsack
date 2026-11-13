package com.CSC4202;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads relief items from a tab-separated file.
 *
 * <p>Each line is {@code name <TAB> weight(g) <TAB> worth}. Blank or malformed lines are
 * skipped with a warning on stderr.
 */
public class ItemReader {

    /** Reads items from a file on disk. */
    public static List<Item> readItems(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            return readItems(in);
        } catch (IOException e) {
            System.err.println("Error reading item file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** Reads items from any stream (e.g. a classpath resource). The stream is not closed. */
    public static List<Item> readItems(InputStream in) {
        List<Item> items = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\t", 3);
                if (parts.length != 3) {
                    System.err.println("Skipping line " + lineNo + ": expected 3 tab-separated columns");
                    continue;
                }
                try {
                    String name = parts[0].trim();
                    int weight = (int) Double.parseDouble(parts[1].trim());
                    int value = Integer.parseInt(parts[2].trim());
                    if (weight < 0 || value < 0) {
                        System.err.println("Skipping line " + lineNo + ": negative weight or worth");
                        continue;
                    }
                    items.add(new Item(name, value, weight));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line " + lineNo + ": weight/worth is not a number");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading item data: " + e.getMessage());
        }
        return items;
    }
}
