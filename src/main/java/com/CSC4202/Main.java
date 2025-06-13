package com.CSC4202;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/java/com/CSC4202/item.tsv";
        int SmallRescueBoat = 35000;
        int MediumRubberBoat = 70000;

        List<Item> items = ItemReader.readItemsFromCSV(filePath);
        if (items.isEmpty()) {
            System.out.println("No items loaded. Check your file format.");
            return;
        }

        System.out.println("\nSmall Rescue Boat");
        // Dynamic Programming
        DPKnapsackSolver.solve(items, SmallRescueBoat);

        System.out.println("\nMedium Rubber Boat");
        // Dynamic Programming
        DPKnapsackSolver.solve(items, MediumRubberBoat);
    }
}
