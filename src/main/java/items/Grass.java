package items;

public record Grass() implements Energizeable {
    @Override
    public int increaseEnergy(int amount) {
        return 0;
    }

    @Override
    public int decreaseEnergy(int amount) {
        return 0;
    }

    @Override
    public String toString() {
        return "📘";
    }
}
