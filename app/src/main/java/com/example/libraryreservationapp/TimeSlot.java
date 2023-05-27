package com.example.libraryreservationapp;

public class TimeSlot {
    private Long slot;
    private int currentCapacity;
    private int totalCapacity;

    public TimeSlot() {
    }

    public TimeSlot(Long slot, int currentCapacity, int totalCapacity) {
        this.slot = slot;
        this.currentCapacity = currentCapacity;
        this.totalCapacity = totalCapacity;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
}

