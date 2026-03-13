package com.example.adventure.utility;

public class Constrained 
{
    private int value;
    private int minValue;
    private int maxValue;

    public Constrained(int value, int minValue, int maxValue) {
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;

        enforceIntegrity();
    }

    public Constrained(int value, int maxValue) {
        this(value, 0, maxValue);
    }

    public Constrained(int maxValue) {
        this(maxValue, maxValue);
    }

    public Constrained(Constrained other) {
        this.value = other.value;
        this.minValue = other.minValue;
        this.maxValue = other.maxValue;
    }

    public int getMinValue() { 
        return minValue; 
    }

    public void setMinValue(int newBound) { 
        minValue = newBound;
        
        enforceIntegrity();
    }

    public int getMaxValue() { 
        return maxValue; 
    }

    public void setMaxValue(int newBound) { 
        maxValue = newBound;
        
        enforceIntegrity();
    }

    public int getValue() { 
        return value; 
    }

    public void increment() { 
        value++;
        constrainValue();
    }

    public void decrement() { 
        value--;
        constrainValue();
    }

    public void increase(int increase) { 
        value += increase;
        constrainValue();
    }

    public void decrease(int decrease) { 
        value -= decrease;
        constrainValue();
    }

    public void change(int difference) { 
        value += difference;
        constrainValue();
    }

    public void changeByRatio(float percentage) { 
        value += Math.round((maxValue - minValue) * percentage);
        constrainValue();
    }

    public void changeToRatio(float percentage) { 
        value = minValue + Math.round((maxValue - minValue) * percentage);
        constrainValue();
    }

    public int getRange() { 
        return maxValue - minValue; 
    }

    public float getRatio() { 
        int range = getRange();
        if (range == 0) return 1f;
        return (float)(value - minValue) / range; 
    }

    public float getReverseRatio() { 
        return 1f - getRatio(); 
    }

    public void reverse() {
        value = maxValue + minValue - value;
        constrainValue();
    }

    public void setValue(int newValue) { 
        value = newValue;
        constrainValue();
    }

    public boolean setValueSafe(int newValue) { 
        if (!constraintCheck(newValue))
            return false;
        value = newValue;
        return true;
    }

    public void minimize() {
        value = minValue;
    }

    public boolean atMinimum() {
        return value == minValue;
    }

    public boolean atMaximum() {
        return value == maxValue;
    }

    public void maximize() {
        value = maxValue;
    }

    private void constrainValue() {
        value = Math.clamp(value, minValue, maxValue);
    }

    private void enforceIntegrity() {
        if (minValue > maxValue) {
            swapBounds();
        }
        constrainValue();
    }

    private boolean constraintCheck(int checkedValue) {
        return checkedValue >= minValue && checkedValue <= maxValue;
    }

    private void swapBounds() {
        int tempBound = minValue;
        minValue = maxValue;
        maxValue = tempBound;
    }
}