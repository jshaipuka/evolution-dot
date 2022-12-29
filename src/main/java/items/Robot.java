package items;

public class Robot implements Item {
    public static final int MAX_ENERGY = 50;
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

    public int decreaseEnergy(int amount) {
        this.energy = Math.max(this.energy - amount, 0);
        return this.energy;
    }

    public int increaseEnergy(int amount) {
        this.energy = Math.min(this.energy + amount, MAX_ENERGY);
        return this.energy;
    }
}

