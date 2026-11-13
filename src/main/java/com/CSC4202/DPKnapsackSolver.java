package com.CSC4202;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves the 0/1 knapsack problem with bottom-up dynamic programming.
 *
 * <p>Recurrence, where {@code dp[i][w]} is the best total value using the first {@code i}
 * items with capacity {@code w}:
 * <pre>
 *   dp[i][w] = dp[i-1][w]                                          if weight_i &gt; w
 *   dp[i][w] = max(dp[i-1][w], dp[i-1][w - weight_i] + value_i)    otherwise
 * </pre>
 * Time and space are both O(n * W).
 */
public class DPKnapsackSolver {

    /** The outcome of one solve: the chosen items and their totals. */
    public record Result(List<Item> selectedItems, int totalWeight, int totalValue, long elapsedMs) {
        public int remainingCapacity(int capacity) {
            return capacity - totalWeight;
        }
    }

    /**
     * Finds the subset of items with the highest total value that fits within the capacity.
     *
     * @param items    candidate items (each can be taken at most once)
     * @param capacity maximum total weight, must not be negative
     * @return the selected items (in original order) and their totals
     */
    public static Result solve(List<Item> items, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must not be negative: " + capacity);
        }
        long startTime = System.currentTimeMillis();

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

        // Backtrack from dp[n][capacity]: if the value changed at row i, item i was taken
        int totalWeight = 0;
        int w = capacity;
        List<Item> selectedItems = new ArrayList<>();
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item item = items.get(i - 1);
                selectedItems.add(0, item); // insert at the beginning to keep original order
                totalWeight += item.getWeight();
                w -= item.getWeight();
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        return new Result(selectedItems, totalWeight, dp[n][capacity], elapsed);
    }

    /** Prints a result in a human-readable form. */
    public static void print(Result result, int capacity) {
        System.out.println("Dynamic Programming Result");
        System.out.println("Speed: " + result.elapsedMs() + " ms\n");

        System.out.println("Total weight:  " + result.totalWeight() + " g");
        System.out.println("Total worth:   " + result.totalValue());
        System.out.println("Remaining capacity: " + result.remainingCapacity(capacity) + " g\n");

        System.out.println("Items selected:");
        int count = 1;
        for (Item item : result.selectedItems()) {
            System.out.println(count++ + ". " + item.getName()
                    + " (Value: " + item.getValue() + ", Weight: " + item.getWeight() + ")");
        }
    }
}
