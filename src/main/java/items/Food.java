package items;

public record Food() implements Item {
    @Override
    public String toString() {
        return "📗";
    }
}
