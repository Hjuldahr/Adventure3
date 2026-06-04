package com.example.adventure.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class RNG {
    private ThreadLocalRandom current() {
        return ThreadLocalRandom.current();
    }

    public float randFloat() {
        return current().nextFloat();
    }

    public float randFloat(int limit) {
        return current().nextFloat(limit);
    }

    public int randInt(int max) {
        return current().nextInt(max);
    }

    public int randInt(int min, int max) {
        return current().nextInt(min, max);
    }

    public boolean randBool() {
        return current().nextBoolean();
    }

    public boolean randBool(int weight) {
        return current().nextFloat() <= weight;
    }

    public <T> List<T> uniqueSample(List<T> choices) {
        if (choices == null || choices.isEmpty()) {
            return List.of();
        }
        List<T> copy = new ArrayList<>(choices);
        Collections.shuffle(copy, current());
        return copy;
    }

    public <T> List<T> uniqueSample(List<T> choices, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        List<T> copy = uniqueSample(choices);
        List<T> subList = copy.subList(0, Math.min(limit, copy.size()));
        return new ArrayList<>(subList);
    }

    public <T> List<T> sample(List<T> choices, int limit) {
        if (choices == null || choices.isEmpty() || limit <= 0) {
            return List.of();
        }
        int size = choices.size();
        return ThreadLocalRandom.current()
            .ints(limit, 0, size)
            .mapToObj(choices::get)
            .toList(); 
    }

    public <T> List<T> sample(List<T> choices) {
        if (choices == null || choices.isEmpty()) {
            return List.of();
        }
        int size = choices.size();
        return ThreadLocalRandom.current()
            .ints(size, 0, size)
            .mapToObj(choices::get)
            .toList(); 
    }
}
