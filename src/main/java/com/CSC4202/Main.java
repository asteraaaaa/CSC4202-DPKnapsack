package com.CSC4202;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Chooses which flood-relief supplies to load onto two boats, maximising total worth.
 *
 * <p>Usage: {@code Main [path/to/items.tsv]}. Without an argument the bundled
 * {@code item.tsv} resource is used.
 */
public class Main {
    private static final String DEFAULT_RESOURCE = "/item.tsv";
    private static final int SMALL_RESCUE_BOAT_CAPACITY_G = 35_000;
    private static final int MEDIUM_RUBBER_BOAT_CAPACITY_G = 70_000;

    public static void main(String[] args) throws IOException {
        List<Item> items;
        if (args.length > 0) {
            items = ItemReader.readItems(Path.of(args[0]));
        } else {
            try (InputStream in = Main.class.getResourceAsStream(DEFAULT_RESOURCE)) {
                if (in == null) {
                    System.out.println("Bundled item list not found: " + DEFAULT_RESOURCE);
                    return;
                }
                items = ItemReader.readItems(in);
            }
        }
        if (items.isEmpty()) {
            System.out.println("No items loaded. Check your file format.");
            return;
        }

        runBoat("Small Rescue Boat", items, SMALL_RESCUE_BOAT_CAPACITY_G);
        runBoat("Medium Rubber Boat", items, MEDIUM_RUBBER_BOAT_CAPACITY_G);
    }

    private static void runBoat(String name, List<Item> items, int capacity) {
        System.out.println("\n" + name + " (capacity " + capacity + " g)");
        DPKnapsackSolver.print(DPKnapsackSolver.solve(items, capacity), capacity);
    }
}
