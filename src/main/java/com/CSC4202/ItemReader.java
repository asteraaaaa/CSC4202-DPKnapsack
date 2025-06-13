package com.CSC4202;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemReader {

    public static List<Item> readItemsFromCSV(String filename) {
        List<Item> items = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\t", 3);
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    int weight  = (int) Double.parseDouble(parts[1].trim());
                    int value = Integer.parseInt(parts[2].trim());
                    items.add(new Item(name, value, weight));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading item file: " + e.getMessage());
        }

        return items;
    }
}
