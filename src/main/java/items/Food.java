package items;

import board.Point;

public record Food() implements Energizeable {
    @Override
    public String toString() {
        return "📗";
    }

    @Override
    public int increaseEnergy(int amount) {
        return 1;
    }

    @Override
    public int decreaseEnergy(int amount) {
        return 0;
    }
}
