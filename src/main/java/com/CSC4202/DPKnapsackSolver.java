package com.CSC4202;

import java.util.ArrayList;
import java.util.List;

public class DPKnapsackSolver {

    public static void solve(List<Item> items, int capacity) {
        long startTime = System.currentTimeMillis(); // Start timing

        int n = items.size();
        int[][] dp = new int[n + 1][capacity + 1];

        // Fill DP table
        for (int i = 1; i <= n; i++) {
            Item item = items.get(i - 1);
            for (int w = 0; w <= capacity; w++) {
                if (item.getWeight() > w) {
                    dp[i][w] = dp[i - 1][w];
                } else {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - item.getWeight()] + item.getValue());
                }
            }
        }

        // Backtrack to find selected items
        int totalWeight = 0;
        int remainingValue = dp[n][capacity];
        int w = capacity;
        List<Item> selectedItems = new ArrayList<>();

        for (int i = n; i > 0 && remainingValue > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item item = items.get(i - 1);
                selectedItems.add(0, item); // insert at the beginning to keep original order
                totalWeight += item.getWeight();
                remainingValue -= item.getValue();
                w -= item.getWeight();
            }
        }

        long endTime = System.currentTimeMillis(); // End timing

        // Output
        System.out.println("Dynamic Programming Result");
        System.out.println("Speed: " + (endTime - startTime) + " ms\n");

        System.out.println("Total weight:  " + totalWeight + " g");
        System.out.println("Total worth:   " + dp[n][capacity]);
        System.out.println("Remaining capacity: " + (capacity - totalWeight) + " g\n");

        System.out.println("Items selected:");
        int count = 1;
        for (Item item : selectedItems) {
            System.out.println(count++ + ". " + item.getName() + " (Value: " + item.getValue() + ", Weight: " + item.getWeight() + ")");
        }
    }
}
