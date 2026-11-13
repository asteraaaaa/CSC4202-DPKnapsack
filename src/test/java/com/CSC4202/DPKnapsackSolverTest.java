package com.CSC4202;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DPKnapsackSolverTest {

    private static Item item(String name, int value, int weight) {
        return new Item(name, value, weight);
    }

    @Test
    void smallKnownCase() {
        // Same example as the project report: W=4, weights [4,5,1], values [1,2,3]
        List<Item> items = List.of(item("a", 1, 4), item("b", 2, 5), item("c", 3, 1));
        DPKnapsackSolver.Result r = DPKnapsackSolver.solve(items, 4);
        assertEquals(3, r.totalValue());
        assertEquals(1, r.totalWeight());
    }

    @Test
    void emptyItemsAndZeroCapacity() {
        assertEquals(0, DPKnapsackSolver.solve(List.of(), 100).totalValue());
        assertEquals(0, DPKnapsackSolver.solve(List.of(item("a", 5, 1)), 0).totalValue());
    }

    @Test
    void allItemsTooHeavy() {
        DPKnapsackSolver.Result r = DPKnapsackSolver.solve(List.of(item("a", 9, 50), item("b", 9, 60)), 10);
        assertEquals(0, r.totalValue());
        assertTrue(r.selectedItems().isEmpty());
    }

    @Test
    void singleItemThatFitsExactly() {
        DPKnapsackSolver.Result r = DPKnapsackSolver.solve(List.of(item("a", 7, 10)), 10);
        assertEquals(7, r.totalValue());
        assertEquals(0, r.remainingCapacity(10));
    }

    @Test
    void negativeCapacityRejected() {
        assertThrows(IllegalArgumentException.class, () -> DPKnapsackSolver.solve(List.of(), -1));
    }

    @Test
    void matchesBruteForceOnRandomInputs() {
        Random rnd = new Random(42);
        for (int trial = 0; trial < 200; trial++) {
            int n = rnd.nextInt(11); // 0..10 items
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                items.add(item("i" + i, rnd.nextInt(50), 1 + rnd.nextInt(30)));
            }
            int capacity = rnd.nextInt(80);

            DPKnapsackSolver.Result r = DPKnapsackSolver.solve(items, capacity);
            assertEquals(bruteForce(items, capacity), r.totalValue(), "trial " + trial);
            // The reported items must really add up to the reported totals and fit
            assertEquals(r.totalValue(), r.selectedItems().stream().mapToInt(Item::getValue).sum());
            assertEquals(r.totalWeight(), r.selectedItems().stream().mapToInt(Item::getWeight).sum());
            assertTrue(r.totalWeight() <= capacity);
        }
    }

    @Test
    void bundledDatasetFitsBothBoats() {
        List<Item> items = ItemReader.readItems(getClass().getResourceAsStream("/item.tsv"));
        assertFalse(items.isEmpty());
        for (int capacity : new int[] {35_000, 70_000}) {
            DPKnapsackSolver.Result r = DPKnapsackSolver.solve(items, capacity);
            assertTrue(r.totalWeight() <= capacity);
            assertTrue(r.totalValue() > 0);
        }
    }

    @Test
    void readerSkipsBadLines() {
        String data = "Water\t1000\t50\n\nbroken line\nRice\tabc\t10\nBlanket\t500.7\t20\n";
        InputStream in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        List<Item> items = ItemReader.readItems(in);
        assertEquals(2, items.size());
        assertEquals("Blanket", items.get(1).getName());
        assertEquals(500, items.get(1).getWeight());
    }

    private static int bruteForce(List<Item> items, int capacity) {
        int best = 0;
        for (int mask = 0; mask < (1 << items.size()); mask++) {
            int weight = 0;
            int value = 0;
            for (int i = 0; i < items.size(); i++) {
                if ((mask & (1 << i)) != 0) {
                    weight += items.get(i).getWeight();
                    value += items.get(i).getValue();
                }
            }
            if (weight <= capacity) {
                best = Math.max(best, value);
            }
        }
        return best;
    }
}
