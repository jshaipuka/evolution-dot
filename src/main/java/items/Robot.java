package items;

import board.Point;

public class Robot implements Energizeable {
    public static final int MAX_ENERGY = 10;
    private int energy;

    public Robot() {
        this(MAX_ENERGY);
    }

    public Robot(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public int decreaseEnergy(int amount) {
        this.energy = Math.max(this.energy - amount, 0);
        return this.energy;
    }

    @Override
    public int increaseEnergy(int amount) {
        this.energy = Math.min(this.energy + amount, MAX_ENERGY);
        return this.energy;
    }

    @Override
    public String toString() {
        return "📕";
    }
}

