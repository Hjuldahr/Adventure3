package com.example.adventure.entity;

public enum SuccessTypes {
    CRIT_FAILURE, 
    FAILURE, 
    SUCCESS, 
    CRIT_SUCCESS;

    /**
     * Increases the degree of success by one step, capping at CRIT_SUCCESS.
     */
    public SuccessTypes stepUp() {
        int next = Math.min(this.ordinal() + 1, CRIT_SUCCESS.ordinal());
        return values()[next];
    }

    /**
     * Decreases the degree of success by one step, capping at CRIT_FAILURE.
     */
    public SuccessTypes stepDown() {
        int next = Math.max(this.ordinal() - 1, CRIT_FAILURE.ordinal());
        return values()[next];
    }
}