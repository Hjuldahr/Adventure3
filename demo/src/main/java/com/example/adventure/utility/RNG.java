package com.example.adventure.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class RNG {
    private static final Random rand = new Random();

    public static boolean randomBool() {
        return rand.nextBoolean();
    }

    public static boolean randomBool(float threshold) {
        return rand.nextFloat() < threshold;
    }
    
    /**
     * Generates 0 (inclusive) to maxValue (exclusive)
     * @param maxValue
     * @return
     */
    public static int randomInt(int maxValue) {
        return rand.nextInt(maxValue);
    }

    /**
     * Generates minValue (inclusive) to maxValue (exclusive)
     * @param minValue
     * @param maxValue 
     * @return
     */
    public static int randomInt(int minValue, int maxValue) {
        return rand.nextInt(minValue, maxValue);
    }

    /**
     * Generates 0f (inclusive) to 1f (inclusive)
     * @return
     */
    public static float randomFloat() {
        return rand.nextFloat();
    }

    /**
     * Generates 0f (inclusive) to maxValue (inclusive)
     * @param minValue
     * @param maxValue
     * @return
     */
    public static float randomFloat(float maxValue) {
        return rand.nextFloat(maxValue);
    }

    /**
     * Generates minValue (inclusive) to maxValue (inclusive)
     * @param minValue
     * @param maxValue
     * @return
     */
    public static float randomFloat(float minValue, float maxValue) {
        return rand.nextFloat(minValue, maxValue);
    }

    /**
     * Return a random single element from an array
     * @param elements
     * @return
     */
    public static <T> T randomElement(List<T> elements) {
        return elements.get(rand.nextInt(elements.size()));
    }

    /**
     * Return a random list of elements from an array without duplicates
     * @param elements
     * @return
     */
    public static <T> List<T> randomElements(int limit, List<T> elements) {
        List<T> temp = new ArrayList<>(elements);
        Collections.shuffle(temp, rand);
        return temp.subList(0, Math.min(limit, temp.size()));
    }

    /**
     * Iterates through a pair of ordered values and cumalative weights (lowest to highest). 
     * @param values
     * @param cumalativeWeights
     * @return
     */
    public static <T> T randomWeightedElement(List<T> values, List<Float> cumulativeWeights) {
        if (values.size() != cumulativeWeights.size()) throw new IllegalArgumentException("Values and Weights are of unequal size");

        float threshold = rand.nextFloat(); // 0.0 to 1.0
        for (int i = 0; i < values.size(); i++) {
            // Find the first "bucket" that the threshold falls into
            if (threshold <= cumulativeWeights.get(i)) {
                return values.get(i);
            }
        }
        // Fallback if weights don't reach 1.0 or list is empty
        return values.isEmpty() ? null : values.get(values.size() - 1);
    }
}
