package com.example.adventure.utility;

public class Timer {
    private int maxDuration;
    private int duration;

    public Timer(int time) {
        this.maxDuration = time;
        this.duration = time;
    }

    public Timer(int duration, int maxDuration) {
        this.maxDuration = maxDuration;
        this.duration = duration;

        if (duration > maxDuration) {
            duration = maxDuration;
        }
    }

    public Timer(Timer other) {
        this.maxDuration = other.maxDuration;
        this.duration = other.duration;
    }

    public void reset() {
        duration = maxDuration;
    }

    public int getDuration() {
        return duration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public float getRatio() { 
        if (maxDuration == 0) return 1f;
        return (float)(duration) / maxDuration; 
    }

    public float getReverseRatio() { 
        return 1f - getRatio(); 
    }

    /**
     * True if at bound
     * @return
     */
    public boolean tickBound() {
        if (duration <= 0) {
            return true;
        }
        duration--;
        return false;
    }

    public boolean tickBound(int amount) {
        if (duration <= 0) {
            return true;
        }
        duration -= amount;
        constrainDuration();
        return false;
    }

    /**
     * Return true if looped
     * @return
     */
    public boolean tickLoop() {
        if (--duration < 0) {
            duration = maxDuration;
            return true;
        }
        return false;
    }

    public boolean tickLoop(int amount) {
        duration -= amount;
        if (duration < 0) {
            duration = maxDuration;
            return true;
        }
        return false;
    }

    /**
     * True if at bound
     * @return
     */
    public boolean untickBound() {
        if (duration >= maxDuration) {
            return true;
        }
        duration++;
        return false;
    }

    public boolean untickBound(int amount) {
        if (duration >= maxDuration) {
            return true;
        }
        duration += amount;
        constrainDuration();
        return false;
    }

    /**
     * Return true if looped
     * @return
     */
    public boolean untickLoop() {
        if (++duration > maxDuration) {
            duration = 0;
            return true;
        }
        return false;
    }

    public boolean untickLoop(int amount) {
        duration += amount;
        if (duration > maxDuration) {
            duration = 0;
            return true;
        }
        return false;
    }

    public boolean isReady() {
        return duration == maxDuration;
    }

    public boolean isFinished() {
        return duration == 0;
    }

    public boolean isRunning() {
        return duration != 0 && duration != maxDuration;
    }

    public void setDuration(int time) {
        duration = time;
        constrainDuration();
    }

    public void setMaxDuration(int time) {
        if (time < 0) time = 0;
        maxDuration = time;
        constrainDuration();
    }

    private void constrainDuration() {
        if (duration < 0) {
            duration = 0;
        } else if (duration > maxDuration) {
            duration = maxDuration;
        }
    }
}
